package org.teiid.test.jdbc.client.proxies;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.teiid.net.CommunicationException;
import org.teiid.net.HostInfo;
import org.teiid.net.socket.ObjectChannelFactory;
import org.teiid.net.socket.OioOjbectChannelFactory;
import org.teiid.net.socket.SocketServerInstanceImpl;

public class SocketServerInstanceProxy {

	public static void main(String[] args) throws CommunicationException, IOException {
		
		ObjectChannelFactory channelFactory = new OioOjbectChannelFactory(System.getProperties());

		HostInfo info = new HostInfo("localhost", new InetSocketAddress("localhost", 31000));
		
		SocketServerInstanceImpl ssii = new SocketServerInstanceImpl(info, 240000, 1000);
		ssii.connect(channelFactory);

	}

}
