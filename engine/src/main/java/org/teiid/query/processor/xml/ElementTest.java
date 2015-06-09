package org.teiid.query.processor.xml;

import java.io.CharArrayWriter;

import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.teiid.core.TeiidComponentException;
import org.teiid.query.mapping.xml.MappingNodeConstants;
import org.teiid.query.processor.xml.NodeDescriptor;
import org.xml.sax.SAXException;

import net.sf.saxon.TransformerFactoryImpl;

public class ElementTest {
	
	private CharArrayWriter streamResultHolder;
	private TransformerHandler handler;
	
	public void setUp() throws TransformerConfigurationException, SAXException {
		streamResultHolder = new CharArrayWriter();
		SAXTransformerFactory factory =  new TransformerFactoryImpl();
		handler = factory.newTransformerHandler();
		handler.setResult(new StreamResult(streamResultHolder));
		handler.startDocument();
	}
	
	public void testStartAndEndEmptyElement() throws TeiidComponentException, SAXException {
		NodeDescriptor descriptor = NodeDescriptor.createNodeDescriptor("E1", null, true, null, null,  null,false, null, MappingNodeConstants.NORMALIZE_TEXT_PRESERVE);
		Element element = new Element(descriptor, handler); 
		element.startElement();
    	element.endElement();
    	handler.endDocument();
    	
    	System.out.println(new String(streamResultHolder.toCharArray()));
	}
	
	public void testStartAndEndElement() throws TeiidComponentException, SAXException {
		NodeDescriptor descriptor = NodeDescriptor.createNodeDescriptor("E1", null, true, null, null,  null,false, null, MappingNodeConstants.NORMALIZE_TEXT_PRESERVE);
		Element element = new Element(descriptor, handler); 
		element.setContent("test");
		element.startElement();
    	element.endElement();
    	handler.endDocument();
    	
    	System.out.println(new String(streamResultHolder.toCharArray()));
	}
	
	public void testAddAttributes() throws TeiidComponentException, SAXException {
		NodeDescriptor descriptor = NodeDescriptor.createNodeDescriptor("E1", null, true, null, null,  null,false, null, MappingNodeConstants.NORMALIZE_TEXT_PRESERVE);
		Element element = new Element(descriptor, handler); 
		descriptor = NodeDescriptor.createNodeDescriptor("a1", null, true, null, null, null, false, null, MappingNodeConstants.NORMALIZE_TEXT_PRESERVE);
		element.setAttribute(descriptor, "test attribute"); 
		element.setContent("test");
		element.startElement();
    	element.endElement();
    	handler.endDocument();
    	
    	System.out.println(new String(streamResultHolder.toCharArray()));
	}

	public static void main(String[] args) throws TeiidComponentException, SAXException, TransformerConfigurationException {
		ElementTest test = new ElementTest();
		test.setUp();
		test.testStartAndEndEmptyElement();
		test.testStartAndEndElement();
		test.testAddAttributes();
	}

}
