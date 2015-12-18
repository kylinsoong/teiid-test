package org.teiid.test.server.netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;

public class NioEchoServer {

	public static void main(String[] args) {

		try {
            service();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
	}
	
	private static void service() throws IOException {

	    Selector selector = Selector.open();
        
        ServerSocketChannel serverSocketChannel= ServerSocketChannel.open();  
        serverSocketChannel.socket().setReuseAddress(true);  
        serverSocketChannel.configureBlocking(false);  
        serverSocketChannel.socket().bind(new InetSocketAddress("localhost", 31000));
        
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT );
        
        Charset charset = Charset.forName("UTF-8"); 
        boolean isRead = false;
        
        while (selector.select() > 0){
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while(iterator.hasNext()){
                SelectionKey key = iterator.next() ;
                iterator.remove();
                printSelectionKey(key);
                            
                if(key.isAcceptable()){
                    ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                    SocketChannel socketChannel = (SocketChannel) ssc.accept();
                    socketChannel.configureBlocking(false); 
                    socketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                }
                
                String read = "";
                
                if (key.isReadable()) {
                    SocketChannel socketChannel = (SocketChannel) key.channel(); 
                    ByteBuffer readBuff = ByteBuffer.allocate(1024);  
                    socketChannel.read(readBuff);  
                    readBuff.flip(); 
                    read = charset.decode(readBuff).toString();
                    isRead = true;
                }
                
                if (key.isWritable() && isRead ) {
                    SocketChannel socketChannel = (SocketChannel) key.channel(); 
                    ByteBuffer buffer = charset.encode("echo: " + read);
                    socketChannel.write(buffer);
                    shutdown(serverSocketChannel);
                }
                
            }
            
        }
    }

    private static void shutdown(ServerSocketChannel serverSocketChannel) throws IOException {
        serverSocketChannel.close();
        System.exit(0);
    }

    static void printSelectionKey(SelectionKey key) {
        System.out.print("channel: " + key.channel());
        System.out.print(", isAcceptable: " + key.isAcceptable());
        System.out.print(", isReadable: " + key.isReadable());
        System.out.print(", isWritable: " + key.isWritable());
        System.out.println(", isValid: " + key.isValid());
    }

}
