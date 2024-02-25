package org.fugerit.java.jsonutil;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.fugerit.java.core.io.helper.HelperIOException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedHashMap;

@Slf4j
public class ProcessJson {

    public void handlePrettyPrint(InputStream is, OutputStream os, ProcessProperty pp ) throws IOException {
        this.handle( is, os, pp, true );
    }

    public void handle(InputStream is, OutputStream os, ProcessProperty pp ) throws IOException {
        this.handle( is, os, pp, false );
    }

    public void handle(InputStream is, OutputStream os, ProcessProperty pp, boolean prettyPrint ) throws IOException {
        HelperIOException.apply( () -> {
            ObjectMapper mapper = new ObjectMapper();
            LinkedHashMap<String, Object> jsonMap = mapper.readValue( is, LinkedHashMap.class );
            ProcessPropertyRecurse ppr = new ProcessPropertyRecurse( pp );
            jsonMap.entrySet().forEach( e -> ppr.processProperty( jsonMap, "", jsonMap, e.getKey(), e.getValue() ) );
            if ( prettyPrint ) {
                mapper.writerWithDefaultPrettyPrinter().writeValue( os, jsonMap );
            } else {
                mapper.writeValue(os, jsonMap);
            }
        } );
    }

}
