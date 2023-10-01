package test.org.fugerit.java.xml2json;

import java.io.File;
import java.io.IOException;
import java.io.Reader;

import org.fugerit.java.core.cfg.ConfigException;
import org.fugerit.java.core.io.helper.StreamHelper;
import org.fugerit.java.core.xml.XMLException;
import org.fugerit.java.xml2json.XmlToJsonHandler;
import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestDefaultXmlConverter {

	private boolean testWorkerDoc( String testId ) throws IOException, ConfigException, XMLException {
		File outputFile = new File( "target/doc_converter_"+testId+".json" );
		String path = "cl://sample/"+testId+".xml";
		log.info( "test worker path : {}, output : {}", path, outputFile.getCanonicalPath() );
		try ( Reader reader = StreamHelper.resolveReader( path ) ) {
			XmlToJsonHandler handler = new XmlToJsonHandler( new DefaultXmlConverterDocSample() );
			JsonNode node = handler.convertToJsonNode(reader);
			handler.getMapper().writerWithDefaultPrettyPrinter().writeValue( outputFile , node );
		}
		return outputFile.exists();
	}
	
	@Test
	public void test1doc() throws ConfigException, IOException, XMLException {
		Assert.assertTrue( this.testWorkerDoc( "doc_test_01" ) );
	}
	
}
