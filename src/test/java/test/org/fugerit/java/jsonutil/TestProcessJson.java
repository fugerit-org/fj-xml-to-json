package test.org.fugerit.java.jsonutil;

import org.fugerit.java.jsonutil.ProcessJson;
import org.fugerit.java.jsonutil.ProcessPropertyLog;
import org.fugerit.java.jsonutil.ProcessPropertyObfuscate;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.util.Arrays;

public class TestProcessJson {

    @Test
    public void testProcessLog() throws IOException {
        File input = new File( "src/test/resources/sample/doc_test_01.json" );
        File output = new File( "target/doc_test_01_nochange.json" );
        try ( InputStream is = Files.newInputStream( input.toPath() );
              OutputStream os = Files.newOutputStream( output.toPath() ) ) {
            ProcessJson pj = new ProcessJson();
            pj.handle( is, os, new ProcessPropertyLog() );
        }
        Assert.assertTrue( output.isFile() );
    }

    @Test
    public void testProcessOfuscate() throws IOException {
        File input = new File( "src/test/resources/sample/doc_test_01.json" );
        File output = new File( "target/doc_test_01_obfuscate.json" );
        try ( InputStream is = Files.newInputStream( input.toPath() );
              OutputStream os = Files.newOutputStream( output.toPath() ) ) {
            ProcessJson pj = new ProcessJson();
            pj.handle( is, os, new ProcessPropertyObfuscate(Arrays.asList( "name" ))  );
        }
        Assert.assertTrue( output.isFile() );
    }

    @Test
    public void testProcessOfuscateAlt() throws IOException {
        File input = new File( "src/test/resources/sample/doc_test_01.json" );
        File output = new File( "target/doc_test_01_obfuscate_sample.json" );
        try ( InputStream is = Files.newInputStream( input.toPath() );
             OutputStream os = Files.newOutputStream( output.toPath() ) ) {
            ProcessJson pj = new ProcessJson();
            pj.handle( is, os, new ProcessPropertyObfuscate( ProcessPropertyObfuscate.SAMPLE_OBFUSCATE_FUN  , Arrays.asList( "_t", "_v" ))  );
        }
        Assert.assertTrue( output.isFile() );
    }

}
