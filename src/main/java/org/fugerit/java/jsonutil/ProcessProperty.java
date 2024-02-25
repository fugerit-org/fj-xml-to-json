package org.fugerit.java.jsonutil;

import java.util.Map;

public interface ProcessProperty {

    public void processProperty(Map<String, Object> jsonRoot, String path, Map<String, Object> jsonParent, String key, Object value );

}
