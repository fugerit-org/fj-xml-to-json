package org.fugerit.java.xml2json;

import java.io.Reader;
import java.io.Writer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.fugerit.java.core.cfg.ConfigException;
import org.fugerit.java.core.xml.dom.DOMIO;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.Getter;

public class XmlToJsonHandler {
	
	public XmlToJsonHandler() {
		this( new ObjectMapper(), new XmlToJsonConverter() );
	}
	
	public XmlToJsonHandler(ObjectMapper mapper) {
		this( mapper, new XmlToJsonConverter() );
	}
	
	public XmlToJsonHandler(XmlToJsonConverter converter) {
		this( new ObjectMapper(), converter );
	}
	
	public XmlToJsonHandler(ObjectMapper mapper, XmlToJsonConverter converter) {
		super();
		this.mapper = mapper;
		this.converter = converter;
	}

	@Getter private ObjectMapper mapper;
	
	@Getter private XmlToJsonConverter converter;

    public XmlToJsonHandler withXmlns( String xmlns ) {
        this.converter.setXmlns( xmlns );
        return this;
    }

	private ObjectNode create( Element currentTag, ObjectNode currentNode ) {
		return this.getConverter().handleTag(this.mapper, currentTag, currentNode);
	}
	
	/*
	 * XML to JSON
	 */
	
	public JsonNode convertToJsonNode( Reader xml ) throws ConfigException {
		return ConfigException.get( () -> {
			Document doc = DOMIO.loadDOMDoc( xml );
			Element root = doc.getDocumentElement();
			return this.convert( root );
		} );
	}
	
	public JsonNode convert( Element root ) {
		return this.create( root, this.mapper.createObjectNode() );
	}
	
	/*
	 * JSON to XML
	 */
	
	public void writerAsXml( Reader jsonReader, Writer writer ) throws ConfigException {
		ConfigException.apply( () -> {
			Element root = this.convertToElement(jsonReader);
			DOMIO.writeDOMIndent( root , writer );
		} );
	}
	
	public Element convertToElement( Reader jsonReader ) throws ConfigException {
		return ConfigException.get( () -> {
			JsonNode node = this.mapper.readTree( jsonReader );
			return this.convert(node);
		} );
	}
	
	public Element convert( JsonNode json ) throws ConfigException {
		return ConfigException.get( () -> {
			DocumentBuilderFactory dbf = DOMIO.newSafeDocumentBuilderFactory();
			dbf.setNamespaceAware( true );
			dbf.setValidating( false );
			DocumentBuilder builder = dbf.newDocumentBuilder();
			Document doc = builder.newDocument();
			Element root = this.getConverter().handleNode(doc, null, json);
			// add root attributes 
			this.getConverter().addAttributes(json, root);
			return root;
		} );
	}
	
}
