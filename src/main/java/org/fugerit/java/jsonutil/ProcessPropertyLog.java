package org.fugerit.java.jsonutil;

import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashMap;

@Slf4j
public class ProcessPropertyLog implements  ProcessProperty {
    @Override
    public void processProperty(LinkedHashMap<String, Object> jsonRoot, String path, LinkedHashMap<String, Object> jsonParent, String key, Object value) {
        log.info( "{} -> {} : {}",  value != null ? value.getClass().getName() : "", path, value );
    }
}
