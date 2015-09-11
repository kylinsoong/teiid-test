package org.teiid.test.jira;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.net.InetSocketAddress;
import java.util.Properties;
import java.util.logging.Level;

import org.teiid.client.security.ILogon;
import org.teiid.client.util.ResultsFuture;
import org.teiid.common.buffer.impl.MemoryStorageManager;
import org.teiid.core.TeiidProcessingException;
import org.teiid.core.util.ObjectConverterUtil;
import org.teiid.example.EmbeddedHelper;
import org.teiid.net.CommunicationException;
import org.teiid.net.ConnectionException;
import org.teiid.net.TeiidURL;
import org.teiid.net.socket.SocketServerConnection;
import org.teiid.net.socket.SocketServerConnectionFactory;
import org.teiid.net.socket.UrlServerDiscovery;
import org.teiid.services.SessionServiceImpl;
import org.teiid.transport.ClientServiceRegistryImpl;
import org.teiid.transport.LogonImpl;
import org.teiid.transport.SSLConfiguration;
import org.teiid.transport.SocketListener;
import org.teiid.transport.SocketListenerStats;


public class TEIID3664 {
	
	SocketListener listener;
	
	private SocketServerConnectionFactory sscf;
	
	private InetSocketAddress addr;
	
	private MemoryStorageManager storageManager;
	
	private SessionServiceImpl service;
	
	public void setUp() {
		addr = new InetSocketAddress(0);
	}
	
	public void tearDown() throws Exception {
		if (listener != null) {
			listener.stop();
		}
	}
	
	public void testConnectWithPooling() throws Exception {
		
		setUp();
		
		SocketServerConnection conn = helpEstablishConnection(false);
		SocketListenerStats stats = listener.getStats();
		assertEquals(2, stats.objectsRead); // handshake response, logon
		assertEquals(1, stats.sockets);
		conn.close();
		stats = listener.getStats();
		assertEquals(1, stats.maxSockets);
		assertEquals(3, stats.objectsRead); // handshake response, logon, logoff
		stats = listener.getStats();
		assertEquals(1, stats.sockets);
		conn = helpEstablishConnection(false);
		conn.close();
		stats = listener.getStats();
		assertEquals(1, stats.sockets);
		assertEquals(1, stats.maxSockets);
		
		tearDown();
	}
	
	private void assertEquals(int a, int b) {
		if(a != b) {
			throw new RuntimeException("assert Error");
		}
	}
	
	private void assertEquals(long a, long b) {
		if(a != b) {
			throw new RuntimeException("assert Error");
		}
	}
	
	private SocketServerConnection helpEstablishConnection(boolean secure) throws CommunicationException, ConnectionException {
		return helpEstablishConnection(secure, new SSLConfiguration(), new Properties());
	}

	private SocketServerConnection helpEstablishConnection(boolean clientSecure, SSLConfiguration config, Properties socketConfig) throws CommunicationException,
			ConnectionException {
		if (listener == null) {
			ClientServiceRegistryImpl server = new ClientServiceRegistryImpl() {
				@Override
				public ClassLoader getCallerClassloader() {
					return getClass().getClassLoader();
				}
			};
			service = new SessionServiceImpl();
			server.registerClientService(ILogon.class, new LogonImpl(service, "fakeCluster"), null); 
//			server.registerClientService(FakeService.class, new FakeServiceImpl(), null);
			storageManager = new MemoryStorageManager();
			listener = new SocketListener(addr, 0, 0, 2, config, server, storageManager);
			
			SocketListenerStats stats = listener.getStats();
			assertEquals(0, stats.maxSockets);
			assertEquals(0, stats.objectsRead);
			assertEquals(0, stats.objectsWritten);
			assertEquals(0, stats.sockets);
		}

		Properties p = new Properties();
		String url = new TeiidURL(addr.getHostName(), listener.getPort(), clientSecure).getAppServerURL();
		p.setProperty(TeiidURL.CONNECTION.SERVER_URL, url); 
		p.setProperty(TeiidURL.CONNECTION.APP_NAME, "test");
		p.setProperty(TeiidURL.CONNECTION.DISCOVERY_STRATEGY, UrlServerDiscovery.class.getName());
		if (sscf == null) {
			sscf = new SocketServerConnectionFactory();
			sscf.initialize(socketConfig);
		}
		return sscf.getConnection(p);
	}

	public static void main(String[] args) throws Exception {
		
		EmbeddedHelper.enableLogger(Level.INFO);

		new TEIID3664().testConnectWithPooling();
	}
	
	public interface FakeService {
		
		ResultsFuture<Integer> asynchResult();
		
		String exceptionMethod() throws TeiidProcessingException;
		
		int lobMethod(InputStream is, Reader r) throws IOException;
		
		Reader getReader() throws IOException;
		
	}
	
	static class FakeServiceImpl implements FakeService {

		public ResultsFuture<Integer> asynchResult() {
			ResultsFuture<Integer> result = new ResultsFuture<Integer>();
			result.getResultsReceiver().receiveResults(new Integer(5));
			return result;
		}

		public String exceptionMethod() throws TeiidProcessingException {
			throw new TeiidProcessingException();
		}
		
		@Override
		public int lobMethod(InputStream is, Reader r) throws IOException {
			return ObjectConverterUtil.convertToByteArray(is).length + ObjectConverterUtil.convertToString(r).length();
		}

		@Override
		public Reader getReader() throws IOException {
			return new StringReader("hello world"); //$NON-NLS-1$
		}
		
	}

}
