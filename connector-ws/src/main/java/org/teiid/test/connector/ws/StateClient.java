package org.teiid.test.connector.ws;

import static javax.xml.stream.XMLStreamConstants.END_DOCUMENT;
import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import javax.activation.DataSource;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stax.StAXSource;
import javax.xml.ws.AsyncHandler;
import javax.xml.ws.Binding;
import javax.xml.ws.Dispatch;
import javax.xml.ws.EndpointReference;
import javax.xml.ws.Response;
import javax.xml.ws.Service;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.Service.Mode;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.http.HTTPBinding;

import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.configuration.security.AuthorizationPolicy;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.ietf.jgss.GSSCredential;


public class StateClient {
    
    private static final String CONNECTION_TIMEOUT = "javax.xml.ws.client.connectionTimeout"; //$NON-NLS-1$
    private static final String RECEIVE_TIMEOUT = "javax.xml.ws.client.receiveTimeout";
    
    static final String GET_ALL = "<GetAllStateInfo xmlns=\"http://www.teiid.org/stateService/\"/>";
    static final String GET_ONE = "<GetStateInfo xmlns=\"http://www.teiid.org/stateService/\"><stateCode xmlns=\"\">CA</stateCode></GetStateInfo>";

    public static void main(String[] args) throws Exception {

//        client_default();
        
//        client_wsdl();
        
        client_http();
        
        client_http_1();
        
        client_http_2();
    }
    
    static void client_http_2() throws IOException {

        String method = "GET" ;
        String endpoint = "http://localhost:8080/customer/getByNumber/161";
        Dispatch<DataSource> dispatch = createDispatch(HTTPBinding.HTTP_BINDING, endpoint, DataSource.class, Mode.MESSAGE);
        dispatch.getRequestContext().put(MessageContext.HTTP_REQUEST_METHOD, method);
        
        DataSource returnValue = dispatch.invoke(null);
        Map<String, Object> rc = dispatch.getResponseContext();
        
        StringBuilder sb = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(returnValue.getInputStream()));
        String line = bufferedReader.readLine();
        while(line != null){
            sb.append(line);
            sb.append('\n');
            line = bufferedReader.readLine();
        }
 
        System.out.println(sb.toString());
    }
    
    static void client_http_1() throws IOException {

        String method = "GET" ;
        String endpoint = "http://localhost:8080/customer/getAll";
        Dispatch<DataSource> dispatch = createDispatch(HTTPBinding.HTTP_BINDING, endpoint, DataSource.class, Mode.MESSAGE);
        dispatch.getRequestContext().put(MessageContext.HTTP_REQUEST_METHOD, method);
        
        DataSource returnValue = dispatch.invoke(null);
        Map<String, Object> rc = dispatch.getResponseContext();
        
        StringBuilder sb = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(returnValue.getInputStream()));
        String line = bufferedReader.readLine();
        while(line != null){
            sb.append(line);
            sb.append('\n');
            line = bufferedReader.readLine();
        }
 
        System.out.println(sb.toString());
    }

    static void client_http() throws IOException {

        String method = "GET" ;
        String endpoint = "http://localhost:8080/customer/customerList";
        Dispatch<DataSource> dispatch = createDispatch(HTTPBinding.HTTP_BINDING, endpoint, DataSource.class, Mode.MESSAGE);
        dispatch.getRequestContext().put(MessageContext.HTTP_REQUEST_METHOD, method);
        
        DataSource returnValue = dispatch.invoke(null);
        Map<String, Object> rc = dispatch.getResponseContext();
        
        StringBuilder sb = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(returnValue.getInputStream()));
        String line = bufferedReader.readLine();
        while(line != null){
            sb.append(line);
            sb.append('\n');
            line = bufferedReader.readLine();
        }
 
        System.out.println(sb.toString());
    }

    static void client_wsdl() throws IOException, XMLStreamException {

        Dispatch<StAXSource> dispatch = createDispatch(StAXSource.class, Mode.PAYLOAD);
        QName opQName = new QName("http://www.teiid.org/stateService/", "GetStateInfo");
        dispatch.getRequestContext().put(MessageContext.WSDL_OPERATION, opQName);
        
        StAXSource source = formStAXSource(GET_ONE);
        StAXSource returnValue = dispatch.invoke(source);
        System.out.println(parseResult(returnValue.getXMLStreamReader()));
        
        source = formStAXSource(GET_ALL);
        returnValue = dispatch.invoke(source);
        System.out.println(parseResult(returnValue.getXMLStreamReader()));
    }

    static void client_default() throws Exception {

        String style = "http://schemas.xmlsoap.org/wsdl/soap/http";
        String action = null;
        String endpoint = "http://localhost:8080/StateService/stateService/StateServiceImpl?WSDL";
        
        Dispatch<StAXSource> dispatch = createDispatch(style, endpoint, StAXSource.class, Mode.PAYLOAD);
        
        StAXSource source = formStAXSource(GET_ONE);
        StAXSource returnValue = dispatch.invoke(source);
        System.out.println(parseResult(returnValue.getXMLStreamReader()));
        
        source = formStAXSource(GET_ALL);
        returnValue = dispatch.invoke(source);
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
    
    private static <T> Dispatch<T> createDispatch(Class<T> type, Mode mode) throws IOException {
        Service wsdlService;
        Bus bus = BusFactory.getThreadDefaultBus();
        BusFactory.setThreadDefaultBus(null);
        try {
            URL url = new URL("http://localhost:8080/StateService/stateService/StateServiceImpl?WSDL");
            QName serviceName = new QName("http://www.teiid.org/stateService/", "stateService");
            wsdlService = Service.create(url, serviceName);
        } finally {
            BusFactory.setThreadDefaultBus(bus);
        }
        
        QName portName = new QName("http://www.teiid.org/stateService/", "StateServiceImplPort");
        Dispatch<T> dispatch = wsdlService.createDispatch(portName, type, mode);
        return dispatch;
    }
    
    private static <T> Dispatch<T> createDispatch(String binding, String endpoint, Class<T> type, Mode mode) {
        
        Dispatch<T> dispatch = null;
        Service svc;
        if (HTTPBinding.HTTP_BINDING.equals(binding) && (type == DataSource.class)){
            Bus bus = BusFactory.getThreadDefaultBus();
            BusFactory.setThreadDefaultBus(null);
            try {
                dispatch = (Dispatch<T>) new HttpDispatch(endpoint, null, "teiid");
            } finally {
                BusFactory.setThreadDefaultBus(bus);
            }
            
            Map<String, List<String>> httpHeaders = (Map<String, List<String>>)dispatch.getRequestContext().get(MessageContext.HTTP_REQUEST_HEADERS);
            if(httpHeaders == null) {
                httpHeaders = new HashMap<String, List<String>>();
            }
            httpHeaders.put("Content-Type", Collections.singletonList("text/xml; charset=utf-8"));//$NON-NLS-1$ //$NON-NLS-2$
            httpHeaders.put("User-Agent", Collections.singletonList("Teiid Server"));//$NON-NLS-1$ //$NON-NLS-2$
            dispatch.getRequestContext().put(MessageContext.HTTP_REQUEST_HEADERS, httpHeaders);
            
        } else {
            Bus bus = BusFactory.getThreadDefaultBus();
            BusFactory.setThreadDefaultBus(null);
            try {
                svc = Service.create(new QName("http://teiid.org", "teiid"));
            } finally {
                BusFactory.setThreadDefaultBus(bus);
            }
            svc.addPort(new QName("http://teiid.org", "teiid"), binding, endpoint);
            
            dispatch = svc.createDispatch(new QName("http://teiid.org", "teiid"), type, mode);
        }
        
        return dispatch;
    }
    
    private static final class HttpDataSource implements DataSource {
        private final URL url;
        private InputStream content;
        private String contentType;

        private HttpDataSource(URL url, InputStream entity, String contentType) {
            this.url = url;
            this.content = entity;
            this.contentType = contentType;
        }

        @Override
        public OutputStream getOutputStream() throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getName() {
            return this.url.getPath();
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return this.content;
        }

        @Override
        public String getContentType() {
            return this.contentType;
        }
    }
    
    private static final class HttpDispatch implements Dispatch<DataSource> {

        private static final String AUTHORIZATION = "Authorization"; //$NON-NLS-1$
        private HashMap<String, Object> requestContext = new HashMap<String, Object>();
        private HashMap<String, Object> responseContext = new HashMap<String, Object>();
        private WebClient client;
        private String endpoint;

        public HttpDispatch(String endpoint, String configFile, @SuppressWarnings("unused") String configName) {
            this.endpoint = endpoint;
            if (configFile == null) {
                this.client = WebClient.create(this.endpoint);
            }
            else {
                this.client = WebClient.create(this.endpoint, configFile);
            }
        }

        @Override
        public DataSource invoke(DataSource msg) {
            try {
                final URL url = new URL(this.endpoint);
                final String httpMethod = (String)this.requestContext.get(MessageContext.HTTP_REQUEST_METHOD);
                
                Map<String, List<String>> header = (Map<String, List<String>>)this.requestContext.get(MessageContext.HTTP_REQUEST_HEADERS);
                for (Map.Entry<String, List<String>> entry : header.entrySet()) {
                    this.client.header(entry.getKey(), entry.getValue().toArray());
                }
                
                if (this.requestContext.get(AuthorizationPolicy.class.getName()) != null) {
                    HTTPConduit conduit = (HTTPConduit)WebClient.getConfig(this.client).getConduit();
                    AuthorizationPolicy policy = (AuthorizationPolicy)this.requestContext.get(AuthorizationPolicy.class.getName());
                    conduit.setAuthorization(policy);
                }
                else if (this.requestContext.get(GSSCredential.class.getName()) != null) {
                    WebClient.getConfig(this.client).getRequestContext().put(GSSCredential.class.getName(), this.requestContext.get(GSSCredential.class.getName()));
                    WebClient.getConfig(this.client).getRequestContext().put("auth.spnego.requireCredDelegation", true); //$NON-NLS-1$ 
                }
//                else if (this.requestContext.get(OAuthCredential.class.getName()) != null) {
//                    OAuthCredential credential = (OAuthCredential)this.requestContext.get(OAuthCredential.class.getName());                    
//                    this.client.header(AUTHORIZATION, credential.getAuthorizationHeader(this.endpoint, httpMethod));
//                }
                
                InputStream payload = null;
                if (msg != null) {
                    payload = msg.getInputStream();
                }
                
                HTTPClientPolicy clientPolicy = WebClient.getConfig(this.client).getHttpConduit().getClient();
                Long timeout = (Long) this.requestContext.get(RECEIVE_TIMEOUT); 
                if (timeout != null) {
                    clientPolicy.setReceiveTimeout(timeout);
                }
                timeout = (Long) this.requestContext.get(CONNECTION_TIMEOUT);
                if (timeout != null) {
                    clientPolicy.setConnectionTimeout(timeout);
                }
                
                javax.ws.rs.core.Response response = this.client.invoke(httpMethod, payload);
                this.responseContext.put("status-code", response.getStatus());
                this.responseContext.putAll(response.getMetadata());

                ArrayList contentTypes = (ArrayList)this.responseContext.get("content-type"); //$NON-NLS-1$
                String contentType = contentTypes != null ? (String)contentTypes.get(0):"application/octet-stream"; //$NON-NLS-1$
                return new HttpDataSource(url, (InputStream)response.getEntity(), contentType);
            } catch (IOException e) {
                throw new WebServiceException(e);
            }
        }

        @Override
        public Map<String, Object> getRequestContext() {
            return this.requestContext;
        }

        @Override
        public Map<String, Object> getResponseContext() {
            return this.responseContext;
        }

        @Override
        public Binding getBinding() {
            throw new UnsupportedOperationException();
        }

        @Override
        public EndpointReference getEndpointReference() {
            throw new UnsupportedOperationException();
        }

        @Override
        public <T extends EndpointReference> T getEndpointReference(Class<T> clazz) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Response<DataSource> invokeAsync(DataSource msg) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Future<?> invokeAsync(DataSource msg,AsyncHandler<DataSource> handler) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void invokeOneWay(DataSource msg) {
            throw new UnsupportedOperationException();
        }
    }


}
