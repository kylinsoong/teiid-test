package org.teiid.test.tmp;

import java.io.FileReader;
import java.io.IOException;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

public class SaxParserExample {
	
	public static void parse(DefaultHandler handler, String file) throws SAXException, IOException {
		XMLReader xreader = XMLReaderFactory.createXMLReader();
		xreader.setContentHandler(handler);
		xreader.setErrorHandler(handler);
		System.out.println(xreader.getClass());
		FileReader reader = new FileReader(file);
		xreader.parse(new InputSource(reader));
	}

	public static void main(String[] args) throws SAXException, IOException {
		DefaultHandler handler = new DefaultHandler(){
			
			private StringBuffer buffer = new StringBuffer();
			
			@Override
			public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
				buffer.setLength(0);
				if (localName.equals("root")) {
					System.out.println("root attr: " + attributes.getValue("attr") + " (" + uri + " - " + localName + " - " + qName + ")");
				} else if (localName.equals("value")) {
					System.out.println("value attr: " + attributes.getValue("attr") + " (" + uri + " - " + localName + " - " + qName + ")");
				}
			}

			@Override
			public void endElement(String uri, String localName, String qName) throws SAXException {
				if (localName.equals("root")) {
					System.out.println("root -> " + buffer.toString() + " (" + uri + " - " + localName + " - " + qName + ")");
				} else if (localName.equals("value")) {
					System.out.println("value ->  " + buffer.toString() + " (" + uri + " - " + localName + " - " + qName + ")");
				}
				buffer.setLength(0);
			}

			@Override
			public void characters(char[] ch, int start, int length) throws SAXException {
				buffer.append(ch, start, length);
			}};
		SaxParserExample.parse(handler, "src/main/resources/sax.xml");
	}

}
