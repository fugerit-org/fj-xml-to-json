package org.fugerit.java.jsonutil;

import java.util.LinkedHashMap;

public interface ProcessProperty {

    public void processProperty( LinkedHashMap<String, Object> jsonRoot, String path, LinkedHashMap<String, Object> jsonParent, String key, Object value );

}
