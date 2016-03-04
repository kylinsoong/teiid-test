package io.netty.example;

import java.nio.channels.spi.SelectorProvider;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

public class Netty4ApiTestExample {

    public static void main(String[] args) {

        NamedThreadFactory nettyPool = new NamedThreadFactory("NIO");
        int maxWorkers = Math.max(4, 2 * Runtime.getRuntime().availableProcessors());
        SelectorProvider provider = SelectorProvider.provider();
        EventLoopGroup workers = new NioEventLoopGroup(maxWorkers, nettyPool, provider);
    }

}
