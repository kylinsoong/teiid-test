package org.teiid.test.server;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.teiid.common.buffer.StorageManager;
import org.teiid.core.TeiidRuntimeException;
import org.teiid.core.util.NamedThreadFactory;
import org.teiid.logging.LogConstants;
import org.teiid.logging.LogManager;
import org.teiid.logging.MessageLevel;
import org.teiid.net.socket.ObjectChannel;
import org.teiid.transport.ChannelListener;
import org.teiid.transport.ClientServiceRegistryImpl;
import org.teiid.transport.SSLAwareChannelHandler;
import org.teiid.transport.SSLConfiguration;
import org.teiid.transport.SocketClientInstance;
import org.teiid.transport.SocketListenerStats;
import org.teiid.transport.ChannelListener.ChannelListenerFactory;

public class MySocketListener implements ChannelListenerFactory {
	
	private SSLAwareChannelHandler channelHandler;
    private Channel serverChanel;
    private boolean isClientEncryptionEnabled;
    private ClientServiceRegistryImpl csr;
	private ServerBootstrap bootstrap;
    
    /**
     * 
     * @param port
     * @param inputBufferSize
     * @param outputBufferSize
     * @param engine null if SSL is disabled
     * @param bindaddress
     * @param server
     */
    public MySocketListener(InetSocketAddress address, int inputBufferSize, int outputBufferSize, int maxWorkers, SSLConfiguration config, ClientServiceRegistryImpl csr, StorageManager storageManager) {
    	
    	if (config != null) {
    		this.isClientEncryptionEnabled = config.isClientEncryptionEnabled();
    	}
    	this.csr = csr;

    	ExecutorService bossExecutor = Executors.newCachedThreadPool(new NamedThreadFactory("bossExecutor")); //$NON-NLS-1$
    	ExecutorService workerExecutor = Executors.newCachedThreadPool(new NamedThreadFactory("workerExecutor")); //$NON-NLS-1$
    	
        if (LogManager.isMessageToBeRecorded(LogConstants.CTX_TRANSPORT, MessageLevel.DETAIL)) { 
            LogManager.logDetail(LogConstants.CTX_TRANSPORT, "server = " + address.getAddress() + "binding to port:" + address.getPort()); //$NON-NLS-1$ //$NON-NLS-2$
		}
        
        if (maxWorkers == 0) {
        	maxWorkers = Math.max(4, 2 * Runtime.getRuntime().availableProcessors());
        }
		
        ChannelFactory factory = new NioServerSocketChannelFactory(bossExecutor, workerExecutor, maxWorkers);
        
        bootstrap = new ServerBootstrap(factory);
        this.channelHandler = createChannelPipelineFactory(config, storageManager);
        bootstrap.setPipelineFactory(channelHandler);
        if (inputBufferSize != 0) {
        	bootstrap.setOption("child.receiveBufferSize", new Integer(inputBufferSize)); //$NON-NLS-1$
        }
        if (outputBufferSize != 0) {
        	bootstrap.setOption("child.sendBufferSize", new Integer(outputBufferSize)); //$NON-NLS-1$
        }
        bootstrap.setOption("child.tcpNoDelay", true); //$NON-NLS-1$
        bootstrap.setOption("child.keepAlive", Boolean.TRUE); //$NON-NLS-1$
        
        this.serverChanel = bootstrap.bind(address);
    }
    
    protected SSLAwareChannelHandler createChannelPipelineFactory(SSLConfiguration config, StorageManager storageManager) {
    	return new SSLAwareChannelHandler(this, config, Thread.currentThread().getContextClassLoader(), storageManager);
    }
    
    public int getPort() {
    	return ((InetSocketAddress)this.serverChanel.getLocalAddress()).getPort();
    }
    
    public void stop() {
    	ChannelFuture future = this.serverChanel.close();
    	bootstrap.shutdown();
    	try {
			future.await();
		} catch (InterruptedException e) {
			throw new TeiidRuntimeException(e);
		}
    }
   
    public SocketListenerStats getStats() {
        SocketListenerStats stats = new SocketListenerStats();             
        stats.objectsRead = this.channelHandler.getObjectsRead();
        stats.objectsWritten = this.channelHandler.getObjectsWritten();
        stats.sockets = this.channelHandler.getConnectedChannels();
        stats.maxSockets = this.channelHandler.getMaxConnectedChannels();
        return stats;
    }

	@Override
	public ChannelListener createChannelListener(ObjectChannel channel) {
		return new SocketClientInstance(channel, csr, this.isClientEncryptionEnabled);
	}

}
