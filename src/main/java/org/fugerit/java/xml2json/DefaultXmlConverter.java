package org.fugerit.java.xml2json;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.fugerit.java.core.lang.helpers.StringUtils;
import org.fugerit.java.core.xml.dom.DOMUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.Getter;

public class DefaultXmlConverter {

	public static final String DEF_PROPERTY_TAG = "_t";
	
	public static final String DEF_PROPERTY_TEXT = "_v";

	public static final String DEF_PROPERTY_ELEMENTS = "_e";
	
	public DefaultXmlConverter() {
		this( DEF_PROPERTY_TAG, DEF_PROPERTY_ELEMENTS, DEF_PROPERTY_TEXT );
	}
	
	public DefaultXmlConverter(String propertyTag, String propertyElements, String propertyText) {
		super();
		this.propertyTag = propertyTag;
		this.propertyElements = propertyElements;
		this.propertyText = propertyText;
	}

	@Getter private String propertyTag;
	
	@Getter private String propertyElements;
	
	@Getter private String propertyText;
	
	public ObjectNode handleTag( ObjectMapper mapper, Element currentTag, ObjectNode currentNode ) {
		// mapping normal properties
		Properties props = DOMUtils.attributesToProperties( currentTag );
		for ( Object k : props.keySet() ) {
			String key = String.valueOf( k );
			currentNode.put( key , props.getProperty( key ));
		}
		// adding special properties
		currentNode.put( this.getPropertyTag() , currentTag.getTagName() );
		if ( currentTag.hasChildNodes() ) {
			NodeList list = currentTag.getChildNodes();
			List<ObjectNode> kids = new ArrayList<>();
			StringBuilder textBuffer = new StringBuilder();
			for ( int k=0; k<list.getLength(); k++ ) {
				Node currentTagChild = list.item( k );
				if ( currentTagChild instanceof Element ) {
					kids.add( this.handleTag(mapper, (Element)currentTagChild, mapper.createObjectNode()) );
				} else if ( currentTagChild instanceof Text ) {
					textBuffer.append( ((Text)currentTagChild).getTextContent() );
				}
			}
			if ( !kids.isEmpty() ) {
				ArrayNode kidsNode = mapper.createArrayNode();
				for ( ObjectNode currentKid : kids ) {
					kidsNode.add( currentKid );
				}
				currentNode.set( this.getPropertyElements(), kidsNode );
			}
			String text = textBuffer.toString();
			if ( StringUtils.isNotEmpty( text ) ) {
				currentNode.put( this.getPropertyText(), text );
			}
		}
		return currentNode;
	}
	
}
