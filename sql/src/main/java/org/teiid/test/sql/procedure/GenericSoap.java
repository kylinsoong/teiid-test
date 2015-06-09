package org.teiid.test.sql.procedure;

import static org.teiid.example.util.IOUtils.findFile;
import static org.teiid.test.util.JDBCUtils.execute;

import java.io.FileInputStream;
import java.sql.Connection;

import org.teiid.resource.adapter.ws.WSManagedConnectionFactory;
import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;
import org.teiid.translator.ws.WSExecutionFactory;

@SuppressWarnings("nls")
public class GenericSoap {
		
	public static void main(String[] args) throws Exception {
		EmbeddedServer es = new EmbeddedServer();
		
		WSExecutionFactory ef = new WSExecutionFactory();
		ef.start();
		es.addTranslator("translator-ws", ef);
		
		//add a connection factory
		WSManagedConnectionFactory wsmcf = new WSManagedConnectionFactory();
		es.addConnectionFactory("java:/StateServiceWebSvcSource", wsmcf.createConnectionFactory());
		
		es.start(new EmbeddedConfiguration());
		
		es.deployVDB(new FileInputStream(findFile("webservice-vdb.xml")));
		
		Connection c = es.getDriver().connect("jdbc:teiid:StateServiceVDB", null);
		
		
		execute(c, "EXEC GetStateInfo('CA', 'http://localhost:8080/StateService/stateService/StateServiceImpl?WSDL')", false);
		
		execute(c, "EXEC GetAllStateInfo('http://localhost:8080/StateService/stateService/StateServiceImpl?WSDL')", true);
				
		es.stop();
	}
	

}
