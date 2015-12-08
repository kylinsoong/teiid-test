package org.teiid.test.server.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.net.InetSocketAddress;
import java.util.concurrent.ThreadFactory;

import org.teiid.common.buffer.BufferManager;
import org.teiid.core.util.NamedThreadFactory;
import org.teiid.transport.ObjectDecoder;
import org.teiid.transport.ObjectEncoder;

public class NettyServerExample {

	public static void main(String[] args) throws InterruptedException {
		
		BufferManager storageManager = BufferManagerFactory.createBufferManager();
		ObjectDecoder decoder =new ObjectDecoder(2097152, 4294967296L, Thread.currentThread().getContextClassLoader(), storageManager);
		
		ThreadFactory nettyPool = new NamedThreadFactory("NIO"); 
		EventLoopGroup workers = new NioEventLoopGroup(8, nettyPool);
		ServerBootstrap bootstrap = new ServerBootstrap();
		bootstrap.group(workers).channel(NioServerSocketChannel.class);
		bootstrap.childHandler(new ChannelInitializer<SocketChannel>(){
			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				ChannelPipeline pipeline = ch.pipeline();
				pipeline.addLast("decoder", decoder); 
				pipeline.addLast("chunker", new ChunkedWriteHandler());
				pipeline.addLast("encoder", new ObjectEncoder());
				pipeline.addLast("handler", new OutputChannelHandler());
			}});
		bootstrap.option(ChannelOption.SO_RCVBUF, new Integer(1024 * 1024));
		bootstrap.option(ChannelOption.SO_SNDBUF, new Integer(1024 * 1024));
		bootstrap.option(ChannelOption.TCP_NODELAY, Boolean.TRUE);
		bootstrap.option(ChannelOption.SO_KEEPALIVE, Boolean.TRUE);
		ChannelFuture serverChannel = bootstrap.bind(new InetSocketAddress("localhost", 31001));
		serverChannel.syncUninterruptibly();
		
		Thread.sleep(Long.MAX_VALUE);
	}

}
