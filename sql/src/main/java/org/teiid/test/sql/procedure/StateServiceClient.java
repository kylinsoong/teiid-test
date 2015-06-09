package org.teiid.test.sql.procedure;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stax.StAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import javax.xml.ws.Service.Mode;

import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;

/**
 * A CXF Dispatch StAXSource Client
 * 
 * 'StateServiceCXFDispatchStAXSourceClient' depend on StateService, more details refer to
 * 		1. https://github.com/kylinsoong/jaxws/tree/master/stateServic
 * 		2. http://ksoong.org/jaxws-stateservice/
 * 
 * @author kylin
 *
 */
public class StateServiceClient {
	
	static final String GET_ALL = "<tns:GetAllStateInfo xmlns:tns=\"http://www.teiid.org/stateService/\"></tns:GetAllStateInfo> ";
	static final String GET_ONE = "<tns:GetStateInfo xmlns:tns=\"http://www.teiid.org/stateService/\"><stateCode>CA</stateCode></tns:GetStateInfo>";

	public static void main(String[] args) throws XMLStreamException, TransformerFactoryConfigurationError, TransformerException {

		final QName serviceName = new QName("http://www.teiid.org/stateService/", "stateService");
		final QName portName = new QName("http://www.teiid.org/stateService/", "StateServiceImplPort");
		
		Bus bus = BusFactory.getThreadDefaultBus();
		BusFactory.setThreadDefaultBus(null);
		Service service;
		try {
			service = Service.create(serviceName);
		} finally {
			BusFactory.setThreadDefaultBus(bus);
		}
		
		String bindingId = "http://schemas.xmlsoap.org/wsdl/soap/http";
		String endpointAddress = "http://localhost:8080/StateService/stateService/StateServiceImpl?WSDL";
		service.addPort(portName, bindingId, endpointAddress);
		Dispatch<StAXSource> dispatch = service.createDispatch(portName, StAXSource.class, Mode.PAYLOAD);
				
		StAXSource returnValue = dispatch.invoke(formStAXSource(GET_ALL));
		System.out.println(parseResult(returnValue.getXMLStreamReader()));
		
		returnValue = dispatch.invoke(formStAXSource(GET_ONE));
		System.out.println(parseResult(returnValue.getXMLStreamReader()));
	}
	
	static String parseResult(XMLStreamReader reader) throws XMLStreamException, TransformerFactoryConfigurationError, TransformerException {
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        StringWriter stringWriter = new StringWriter();
        transformer.transform(new StAXSource(reader), new StreamResult(stringWriter));
        return stringWriter.toString();
	}
	
	static StAXSource formStAXSource(String xml) throws XMLStreamException {
		XMLInputFactory factory = XMLInputFactory.newInstance();
		StAXSource source = new StAXSource(factory.createXMLStreamReader(new ByteArrayInputStream(xml.getBytes())));
		return source;
	}

}
