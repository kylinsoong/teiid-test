package org.teiid.test.embedded.netty;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.teiid.core.util.NamedThreadFactory;

public class NettyServer {
    

    public static void main(String[] args) throws InterruptedException {

        ExecutorService nettyPool = Executors.newCachedThreadPool(new NamedThreadFactory("NIO"));
        ChannelFactory factory = new NioServerSocketChannelFactory(nettyPool, nettyPool, 8);
        ServerBootstrap bootstrap = new ServerBootstrap(factory);
        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            @Override
            public ChannelPipeline getPipeline() throws Exception {
                return Channels.pipeline(new SystemoutChannelHandler());
            }});
        bootstrap.setOption("child.tcpNoDelay", true);
        bootstrap.setOption("child.keepAlive", Boolean.TRUE);
        Channel serverChanel = bootstrap.bind(new InetSocketAddress("localhost", 31000));
        
        Thread.sleep(Long.MAX_VALUE);
    }
    
    

}
