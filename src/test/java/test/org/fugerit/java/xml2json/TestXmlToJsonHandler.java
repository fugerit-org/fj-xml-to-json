package test.org.fugerit.java.xml2json;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;

import org.fugerit.java.core.cfg.ConfigException;
import org.fugerit.java.core.io.helper.StreamHelper;
import org.fugerit.java.core.xml.XMLException;
import org.fugerit.java.xml2json.XmlToJsonHandler;
import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestXmlToJsonHandler {

	/*
	 * Test XML to JSON
	 */
	
	private boolean testWorkerXmlToJson( String testId ) throws IOException, ConfigException, XMLException {
		File outputFile = new File( "target/"+testId+".json" );
		String path = "cl://sample/"+testId+".xml";
		log.info( "test worker path : {}, output : {}", path, outputFile.getCanonicalPath() );
		try ( Reader reader = StreamHelper.resolveReader( path ) ) {
			XmlToJsonHandler handler = new XmlToJsonHandler();
			JsonNode node = handler.convertToJsonNode(reader);
			handler.getMapper().writerWithDefaultPrettyPrinter().writeValue( outputFile , node );
		}
		return outputFile.exists();
	}
	
	@Test
	public void test1json() throws ConfigException, IOException, XMLException {
		Assert.assertTrue( this.testWorkerXmlToJson( "doc_test_01" ) );
	}
	
	@Test
	public void test2json() throws ConfigException, IOException, XMLException {
		Assert.assertTrue( this.testWorkerXmlToJson( "minimal" ) );
	}
	
	/*
	 * Test JSON to XML
	 */
	
	private boolean testWorkerJsonToXml( String testId ) throws IOException, ConfigException, XMLException {
		File outputFile = new File( "target/"+testId+".xml" );
		String path = "cl://sample/"+testId+".json";
		log.info( "test worker path : {}, output : {}", path, outputFile.getCanonicalPath() );
		try ( Reader reader = StreamHelper.resolveReader( path );
				FileWriter writer = new FileWriter(outputFile) ) {
			XmlToJsonHandler handler = new XmlToJsonHandler();
			handler.writerAsXml(reader, writer);
		}
		return outputFile.exists();
	}
	
	@Test
	public void test1xml() throws ConfigException, IOException, XMLException {
		Assert.assertTrue( this.testWorkerJsonToXml( "doc_test_01" ) );
	}
	
	@Test
	public void test2xmlFail1() throws ConfigException, IOException, XMLException {
		Assert.assertThrows( ConfigException.class , () -> this.testWorkerJsonToXml( "fail_01" ) );
	}

	@Test
	public void test3xmlFail2() throws ConfigException, IOException, XMLException {
		Assert.assertThrows( ConfigException.class , () -> this.testWorkerJsonToXml( "fail_02" ) );
	}
	
	/*
	 * Test XML to YAML
	 */
	
	private boolean testWorkerYaml( String testId ) throws IOException, ConfigException, XMLException {
		File outputFile = new File( "target/"+testId+".yaml" );
		String path = "cl://sample/"+testId+".xml";
		log.info( "test worker path : {}, output : {}", path, outputFile.getCanonicalPath() );
		try ( Reader reader = StreamHelper.resolveReader( path ) ) {
			XmlToJsonHandler handler = new XmlToJsonHandler( new YAMLMapper() );
			JsonNode node = handler.convertToJsonNode(reader);
			handler.getMapper().writerWithDefaultPrettyPrinter().writeValue( outputFile , node );
		}
		return outputFile.exists();
	}
	
	@Test
	public void test1yaml() throws ConfigException, IOException, XMLException {
		Assert.assertTrue( this.testWorkerYaml( "doc_test_01" ) );
	}
	
	@Test
	public void test2yaml() throws ConfigException, IOException, XMLException {
		Assert.assertTrue( this.testWorkerYaml( "minimal" ) );
	}
	
}
