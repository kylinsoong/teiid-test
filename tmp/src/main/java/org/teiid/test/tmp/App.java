package org.teiid.test.tmp;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;

public class App {
	
	public static void main(String[] args) throws TransformerConfigurationException, TransformerFactoryConfigurationError {
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		
		System.out.println(transformer);
	}

}
