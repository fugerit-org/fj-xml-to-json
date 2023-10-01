package org.fugerit.java.xml2json;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.fugerit.java.core.cfg.ConfigException;
import org.fugerit.java.core.lang.helpers.StringUtils;
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

public class XmlToJsonConverter {

	public static final String DEF_PROPERTY_TAG = "_t";
	
	public static final String DEF_PROPERTY_TEXT = "_v";

	public static final String DEF_PROPERTY_ELEMENTS = "_e";
	
	public XmlToJsonConverter() {
		this( DEF_PROPERTY_TAG, DEF_PROPERTY_ELEMENTS, DEF_PROPERTY_TEXT );
	}
	
	public XmlToJsonConverter(String propertyTag, String propertyElements, String propertyText) {
		super();
		this.propertyTag = propertyTag;
		this.propertyElements = propertyElements;
		this.propertyText = propertyText;
		this.specialProperties = new HashSet<>( Arrays.asList( propertyTag, propertyElements, propertyText ) );
	}

	@Getter private String propertyTag;
	
	@Getter private String propertyElements;
	
	@Getter private String propertyText;
	
	private Set<String> specialProperties;
	
	/*
	 * XML to JSON Conversion 
	 */
	
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
	
	/*
	 * JSON to XML Conversion 
	 */
	
	private void iterateElement( JsonNode current, Document doc, Element tag ) throws ConfigException {
		JsonNode elementsNode = current.get( this.getPropertyElements() );
		if ( elementsNode != null ) {
			if ( elementsNode.isArray() ) {
				Iterator<JsonNode> itElements = elementsNode.elements();
				while ( itElements.hasNext() ) {
					JsonNode currentElement = itElements.next();
					this.handleNode(doc, tag, currentElement);
				}
			} else {
				throw new ConfigException( "Property must be an array : "+elementsNode );
			}
		}
	}
	
	public void addAttributes( JsonNode current, Element tag ) {
		Iterator<String> itNames = current.fieldNames();
		while ( itNames.hasNext() ) {
			String currentName = itNames.next();
			if ( !this.specialProperties.contains( currentName ) ) {
				tag.setAttribute( currentName , current.get( currentName ).asText() );
			}
		}
	}
	
	public Element handleNode( Document doc, Element parent, JsonNode current ) throws ConfigException {
		Element tag = null;
		JsonNode tagNode = current.get( this.getPropertyTag() );
		if ( tagNode == null ) {
			throw new ConfigException( "Tag node is null : "+this.getPropertyTag() );
		} else {
			String tagName = tagNode.asText();
			tag = doc.createElement( tagName );
			if ( parent != null ) {
				parent.appendChild( tag );
			}
			JsonNode textNode = current.get( this.getPropertyText() );
			if ( textNode != null ) {
				tag.appendChild( doc.createTextNode( textNode.asText() ) );
			}
			this.iterateElement(current, doc, tag);
			this.addAttributes(current, tag);
		}
		return tag;
	}

}
