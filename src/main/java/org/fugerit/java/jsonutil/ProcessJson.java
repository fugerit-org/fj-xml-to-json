package org.fugerit.java.jsonutil;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.fugerit.java.core.io.helper.HelperIOException;

import java.io.*;
import java.util.LinkedHashMap;

@Slf4j
public class ProcessJson {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Getter
    private ObjectWriter writer;

    public ProcessJson(ObjectWriter writer) {
        this.writer = writer;
    }

    public ProcessJson() {
        this( MAPPER.writerWithDefaultPrettyPrinter()  );
    }

    public void handle(Reader is, Writer os, ProcessProperty pp ) throws IOException {
        HelperIOException.apply( () -> {
            ObjectMapper mapper = new ObjectMapper();
            LinkedHashMap<String, Object> jsonMap = mapper.readValue( is, LinkedHashMap.class );
            ProcessPropertyRecurse ppr = new ProcessPropertyRecurse( pp );
            jsonMap.entrySet().forEach( e -> ppr.processProperty( jsonMap, "", jsonMap, e.getKey(), e.getValue() ) );
            this.writer.writeValue( os, jsonMap );
         } );
    }

    public void handle(InputStream is, OutputStream os, ProcessProperty pp ) throws IOException {
        try ( Reader reader = new InputStreamReader( is );
                Writer writer = new OutputStreamWriter( os ) ) {
            this.handle( reader, writer, pp );
        }
    }

}
