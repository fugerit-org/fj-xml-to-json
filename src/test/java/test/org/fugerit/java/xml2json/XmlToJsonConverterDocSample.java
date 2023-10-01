package test.org.fugerit.java.xml2json;

import org.fugerit.java.xml2json.XmlToJsonConverter;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

public class XmlToJsonConverterDocSample extends XmlToJsonConverter {

	private static final String TAG_INFO = "info";
	
	private boolean isMetaSection( String tagName ) {
		return  "meta".equals( tagName ) || "metadata".equals( tagName );
	}
	
	@Override
	public ObjectNode handleTag(ObjectMapper mapper, Element currentTag, ObjectNode currentNode) {
		String tagName = currentTag.getTagName();
		// special handling of metadata section
		if ( this.isMetaSection(tagName) ) {
			NodeList list = currentTag.getChildNodes();
			ArrayNode kidsNode = mapper.createArrayNode();
			ArrayNode infoNode = mapper.createArrayNode();
			for ( int k=0; k<list.getLength(); k++ ) {
				Node kidNode = list.item(k);
				if ( kidNode instanceof Element ) {
					Element kidTag = (Element)kidNode;
					if ( TAG_INFO.equals( kidTag.getTagName() ) ) {
						ObjectNode currentInfo = mapper.createObjectNode();
						currentInfo.set( kidTag.getAttribute( "name" ) , new TextNode( kidTag.getTextContent() ) );
						infoNode.add( currentInfo );
					} else {
						kidsNode.add( super.handleTag(mapper, kidTag, mapper.createObjectNode() ) );
					}
				}
			}
			if ( !infoNode.isEmpty() ) {
				currentNode.set( TAG_INFO , infoNode );
			}
			currentNode.set( this.getPropertyElements(), kidsNode );
		} else {
			currentNode = super.handleTag(mapper, currentTag, currentNode);
		}
		return currentNode;
	}

}
