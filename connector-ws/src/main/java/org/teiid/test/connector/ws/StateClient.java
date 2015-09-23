package org.teiid.test.connector.ws;

import static javax.xml.stream.XMLStreamConstants.END_DOCUMENT;
import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stax.StAXSource;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import javax.xml.ws.Service.Mode;

import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;

public class StateClient {
    
    static final String GET_ALL = "<GetAllStateInfo xmlns=\"http://www.teiid.org/stateService/\"/>";
    static final String GET_ONE = "<GetStateInfo xmlns=\"http://www.teiid.org/stateService/\"><stateCode xmlns=\"\">CA</stateCode></GetStateInfo>";

    public static void main(String[] args) throws Exception {

        client_default();
    }

    static void client_default() throws Exception {

        String style = "http://schemas.xmlsoap.org/wsdl/soap/http";
        String action = null;
        StAXSource source = formStAXSource(GET_ALL);
        String endpoint = "http://localhost:8080/StateService/stateService/StateServiceImpl?WSDL";
        
        Dispatch<StAXSource> dispatch = createDispatch(style, endpoint, StAXSource.class, Mode.PAYLOAD);
        
        StAXSource returnValue = dispatch.invoke(source);
        System.out.println(parseResult(returnValue.getXMLStreamReader()));
    }
    
    static List<String> parseResult(XMLStreamReader reader) throws XMLStreamException {
        List<String> stateNames = new ArrayList<String>();

        while (true) {
                if (reader.getEventType() == END_DOCUMENT) {
                        break;
                }
                if (reader.getEventType() == START_ELEMENT) {
                        String cursor = reader.getLocalName();
                        if (cursor.equals("Name")) {
                                reader.next();
                                String value = reader.getText();
                                stateNames.add(value);
                        }
                }
                reader.next();
        }

        return stateNames;
    }

    
    private static StAXSource formStAXSource(String xml) throws XMLStreamException {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        StAXSource source = new StAXSource(factory.createXMLStreamReader(new ByteArrayInputStream(xml.getBytes())));
        return source;
    }
    
    private static <T> Dispatch<T> createDispatch(String binding, String endpoint, Class<T> type, Mode mode) {
        
        Bus bus = BusFactory.getThreadDefaultBus();
        BusFactory.setThreadDefaultBus(null);
        
        Dispatch<T> dispatch = null;
        Service svc;
        try {
            svc = Service.create(new QName("http://teiid.org", "teiid"));
        } finally {
            BusFactory.setThreadDefaultBus(bus);
        }
        svc.addPort(new QName("http://teiid.org", "teiid"), binding, endpoint);
        
        dispatch = svc.createDispatch(new QName("http://teiid.org", "teiid"), type, mode);
        
        return dispatch;
    }


}
