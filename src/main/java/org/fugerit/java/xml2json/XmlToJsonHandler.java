package org.fugerit.java.xml2json;

import java.io.Reader;

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
		this( new ObjectMapper(), new DefaultXmlConverter() );
	}
	
	public XmlToJsonHandler(ObjectMapper mapper) {
		this( mapper, new DefaultXmlConverter() );
	}
	
	public XmlToJsonHandler(DefaultXmlConverter converter) {
		this( new ObjectMapper(), converter );
	}
	
	public XmlToJsonHandler(ObjectMapper mapper, DefaultXmlConverter converter) {
		super();
		this.mapper = mapper;
		this.converter = converter;
	}

	@Getter private ObjectMapper mapper;
	
	@Getter private DefaultXmlConverter converter;

	private ObjectNode create( Element currentTag, ObjectNode currentNode ) {
		return this.getConverter().handleTag(this.mapper, currentTag, currentNode);
	}
	
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
	
}
