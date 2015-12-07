package org.teiid.test.embedded.netty;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jboss.netty.channel.socket.nio.BossPool;
import org.jboss.netty.channel.socket.nio.NioServerBoss;
import org.jboss.netty.channel.socket.nio.NioServerBossPool;
import org.jboss.netty.channel.socket.nio.NioWorker;
import org.jboss.netty.channel.socket.nio.NioWorkerPool;
import org.jboss.netty.channel.socket.nio.WorkerPool;
import org.teiid.core.util.NamedThreadFactory;

public class NettyServerDebug {

    public static void main(String[] args) {
        
        ExecutorService nettyPool = Executors.newCachedThreadPool(new NamedThreadFactory("NIO"));
        
        WorkerPool<NioWorker> workerPool = new NioWorkerPool(nettyPool, 8);
        
        BossPool<NioServerBoss> bossPool = new NioServerBossPool(nettyPool, 1, null);
    }
}
