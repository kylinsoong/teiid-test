package io.netty.example;

import java.net.InetSocketAddress;
import java.nio.channels.spi.SelectorProvider;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;

public class Netty4ApiTestExample {

    public static void main(String[] args) {

        NamedThreadFactory nettyPool = new NamedThreadFactory("NIO");
        int maxWorkers = Math.max(4, 2 * Runtime.getRuntime().availableProcessors());
        SelectorProvider provider = SelectorProvider.provider();
        EventLoopGroup workers = new NioEventLoopGroup(maxWorkers, nettyPool, provider);
    
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(workers).channel(NioServerSocketChannel.class);
        bootstrap.childHandler(new ChannelInitializer<SocketChannel>(){

            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast("chunker", new ChunkedWriteHandler());
//                pipeline.addLast("decoder", new ObjectDecoder());
                pipeline.addLast("encoder", new ObjectEncoder());        
                pipeline.addLast("handler", new EchoServerHandler());
            }});
        
        bootstrap.childOption(ChannelOption.TCP_NODELAY, Boolean.TRUE); 
        bootstrap.childOption(ChannelOption.SO_KEEPALIVE, Boolean.TRUE); 
        ChannelFuture future = bootstrap.bind(new InetSocketAddress("localhost", 31000));
        future.syncUninterruptibly();
        Channel serverChannel = future.channel();
        
        
        System.out.println(serverChannel);
    }

}
