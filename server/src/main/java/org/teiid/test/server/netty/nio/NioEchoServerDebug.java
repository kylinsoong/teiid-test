package org.teiid.test.server.netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;

public class NioEchoServerDebug {

	public static void main(String[] args) throws IOException {
	    
	    Selector selector = Selector.open();
	    
	    ServerSocketChannel serverSocketChannel= ServerSocketChannel.open();
	    serverSocketChannel.socket().setReuseAddress(true);  
	    serverSocketChannel.configureBlocking(false);  
	    serverSocketChannel.socket().bind(new InetSocketAddress("localhost", 31000));
        
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT );
        
        Charset charset = Charset.forName("UTF-8"); 
        boolean isRead = false;
        
        for(;;){
            System.out.println(selector.select());
            System.out.println(selector.isOpen());
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while(iterator.hasNext()){
                SelectionKey key = iterator.next() ;
                printSelectionKey(key);
                iterator.remove();
                System.out.println(selector.selectedKeys());
            }
//            System.out.println(selector.selectedKeys().iterator().next());
        }
        
	}

    private static void printSelectionKey(SelectionKey key) {
        System.out.print("channel: " + key.channel());
        System.out.print(", isAcceptable: " + key.isAcceptable());
        System.out.print(", isReadable: " + key.isReadable());
        System.out.print(", isWritable: " + key.isWritable());
        System.out.println(", isValid: " + key.isValid());
    }

}
