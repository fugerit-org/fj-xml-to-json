package test.org.fugerit.java.jsonutil;

import org.fugerit.java.jsonutil.ProcessJson;
import org.fugerit.java.jsonutil.ProcessPropertyLog;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;

public class TestProcessJson {

    @Test
    public void testProcessLog() throws IOException {
        File input = new File( "src/test/resources/sample/doc_test_01.json" );
        File output = new File( "target/doc_test_01_nochange.json" );
        try (FileInputStream is = new FileInputStream( input );
             OutputStream os = new FileOutputStream( output ) ) {
            ProcessJson pj = new ProcessJson();
            pj.handle( is, os, new ProcessPropertyLog() );
        }
        Assert.assertTrue( output.isFile() );
    }

}
