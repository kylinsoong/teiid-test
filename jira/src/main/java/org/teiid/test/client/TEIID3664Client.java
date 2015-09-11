package org.teiid.test.client;

import java.util.Properties;

import org.teiid.net.CommunicationException;
import org.teiid.net.ConnectionException;
import org.teiid.net.TeiidURL;
import org.teiid.net.socket.SocketServerConnection;
import org.teiid.net.socket.SocketServerConnectionFactory;
import org.teiid.net.socket.UrlServerDiscovery;

public class TEIID3664Client {

	public static void main(String[] args) throws CommunicationException, ConnectionException {

		Properties p = new Properties();
		String url = new TeiidURL("127.0.0.1", 31000, false).getAppServerURL();
		p.setProperty(TeiidURL.CONNECTION.SERVER_URL, url); 
		p.setProperty(TeiidURL.CONNECTION.APP_NAME, "test");
		p.setProperty(TeiidURL.CONNECTION.DISCOVERY_STRATEGY, UrlServerDiscovery.class.getName());
		
		SocketServerConnectionFactory sscf = new SocketServerConnectionFactory();
		
		sscf.initialize(new Properties());
		
		SocketServerConnection conn = sscf.getConnection(p);
		
		conn.close();
		
		conn = null;
		
		System.out.println("DONE");
	}

}
