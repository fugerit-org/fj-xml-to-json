package test.org.fugerit.java.xml2json;

import java.io.File;
import java.io.IOException;
import java.io.Reader;

import org.fugerit.java.core.cfg.ConfigException;
import org.fugerit.java.core.io.FileIO;
import org.fugerit.java.core.io.helper.StreamHelper;
import org.fugerit.java.core.xml.XMLException;
import org.fugerit.java.core.xml.dom.DOMIO;
import org.fugerit.java.xml2json.XmlToJsonHandler;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestXmlToJsonHandler {

	private boolean testWorker( String testId ) throws IOException, ConfigException, XMLException {
		File outputFile = new File( "target/"+testId+".json" );
		String path = "cl://sample/"+testId+".xml";
		log.info( "test worker path : {}, output : {}", path, outputFile.getCanonicalPath() );
		try ( Reader reader = StreamHelper.resolveReader( path ) ) {
			Document doc = DOMIO.loadDOMDoc( reader );
			XmlToJsonHandler handler = new XmlToJsonHandler();
			JsonNode node = handler.convert( doc.getDocumentElement() );
			FileIO.writeString( node.toPrettyString() , outputFile );
		}
		return outputFile.exists();
	}
	
	@Test
	public void test1() throws ConfigException, IOException, XMLException {
		Assert.assertTrue( this.testWorker( "doc_test_01" ) );
	}
	
	@Test
	public void test2() throws ConfigException, IOException, XMLException {
		Assert.assertTrue( this.testWorker( "minimal" ) );
	}
	
}