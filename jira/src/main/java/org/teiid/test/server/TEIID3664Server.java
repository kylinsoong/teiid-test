package org.teiid.test.server;

import java.net.InetSocketAddress;

import org.teiid.client.security.ILogon;
import org.teiid.common.buffer.impl.MemoryStorageManager;
import org.teiid.services.SessionServiceImpl;
import org.teiid.transport.ClientServiceRegistryImpl;
import org.teiid.transport.LogonImpl;
import org.teiid.transport.SSLConfiguration;
import org.teiid.transport.SocketListenerStats;

public class TEIID3664Server {

	public static void main(String[] args) throws InterruptedException {

		InetSocketAddress addr = new InetSocketAddress("127.0.0.1", 31000);
		
		ClientServiceRegistryImpl server = new ClientServiceRegistryImpl() {
			@Override
			public ClassLoader getCallerClassloader() {
				return getClass().getClassLoader();
			}
		};
		
		SessionServiceImpl service = new SessionServiceImpl();
		server.registerClientService(ILogon.class, new LogonImpl(service, "fakeCluster"), null); 
		
		MemoryStorageManager storageManager = new MemoryStorageManager();
		
		SSLConfiguration config = new SSLConfiguration();
		MySocketListener listener = new MySocketListener(addr, 0, 0, 1, config, server, storageManager);
		
		while(true) {
			Thread.sleep(1000 * 2);
			SocketListenerStats stats = listener.getStats();
			System.out.println("objectsRead: " + stats.objectsRead + ", objectsWritten: " + stats.objectsWritten + ", sockets: " + stats.sockets + ", maxSockets: " + stats.maxSockets);
		}
		
	}

}
