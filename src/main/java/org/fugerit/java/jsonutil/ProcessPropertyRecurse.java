package org.fugerit.java.jsonutil;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ProcessPropertyRecurse implements ProcessProperty {

    public ProcessPropertyRecurse(ProcessProperty pp) {
        this.pp = pp;
    }

    private ProcessProperty pp;

    @Override
    public void processProperty(Map<String, Object> jsonRoot, String path, Map<String, Object> jsonParent, String key, Object value) {
        if ( value instanceof  LinkedHashMap ) {
            LinkedHashMap<String, Object> newParant = ((LinkedHashMap<String, Object>) value);
            newParant.entrySet().forEach(
                    e -> this.processProperty(jsonRoot, path + "." + e.getKey(), newParant, e.getKey(), e.getValue())
            );
        } else if ( value instanceof List) {
            List<Object> list = (List<Object>) value;
            list.forEach( e -> this.processProperty( jsonRoot, path, jsonRoot, key, e ) );
        } else {
            this.pp.processProperty( jsonRoot, path, jsonParent, key, value );
        }
    }
}
