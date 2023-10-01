package org.fugerit.java.xml2json;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.fugerit.java.core.cfg.ConfigException;
import org.fugerit.java.core.lang.helpers.StringUtils;
import org.fugerit.java.core.xml.dom.DOMIO;
import org.fugerit.java.core.xml.dom.DOMUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.Getter;

public class XmlToJsonHandler {
	
	public XmlToJsonHandler() {
		this( new ObjectMapper() );
	}
	
	public XmlToJsonHandler(ObjectMapper mapper) {
		super();
		this.mapper = mapper;
	}

	@Getter private ObjectMapper mapper;

	private ObjectNode create( Element currentTag, ObjectNode currentNode ) throws Exception {
		// mapping normal properties
		Properties props = DOMUtils.attributesToProperties( currentTag );
		for ( Object k : props.keySet() ) {
			String key = String.valueOf( k );
			currentNode.put( key , props.getProperty( key ));
		}
		// adding special properties
		currentNode.put( ObjectMapperHelper.PROPERTY_TAG , currentTag.getTagName() );
		if ( currentTag.hasChildNodes() ) {
			NodeList list = currentTag.getChildNodes();
			List<ObjectNode> kids = new ArrayList<>();
			StringBuilder textBuffer = new StringBuilder();
			for ( int k=0; k<list.getLength(); k++ ) {
				Node currentTagChild = list.item( k );
				if ( currentTagChild instanceof Element ) {
					kids.add( this.create((Element)currentTagChild, this.mapper.createObjectNode()) );
				} else if ( currentTagChild instanceof Text ) {
					textBuffer.append( ((Text)currentTagChild).getTextContent() );
				}
			}
			if ( !kids.isEmpty() ) {
				ArrayNode kidsNode = this.mapper.createArrayNode();
				for ( ObjectNode currentKid : kids ) {
					kidsNode.add( currentKid );
				}
				currentNode.set( ObjectMapperHelper.PROPERTY_ELEMENTS, kidsNode );
			}
			String text = textBuffer.toString();
			if ( StringUtils.isNotEmpty( text ) ) {
				currentNode.put( ObjectMapperHelper.PROPERTY_TEXT, text );
			}
		}
		return currentNode;
	}
	
	public JsonNode convertToJsonNode( Reader xml ) throws ConfigException {
		return ConfigException.get( () -> {
			Document doc = DOMIO.loadDOMDoc( xml );
			Element root = doc.getDocumentElement();
			return this.convert( root );
		} );
	}
	
	public JsonNode convert( Element root ) throws ConfigException {
		return ConfigException.get( () -> this.create( root, this.mapper.createObjectNode() ) );
	}
	
}
