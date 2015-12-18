package org.teiid.test.server.netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;

public class NioEchoClient {

	public static void main(String[] args) {

		try {
            service();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        
	}

    private static void service() throws IOException {

        Selector selector = Selector.open();
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("localhost", 31000));  
        socketChannel.configureBlocking(false); 
        
        socketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE); 
        
        Charset charset = Charset.forName("UTF-8"); 
        boolean isWrite = true;
        
        while (selector.select() > 0){
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();
                NioEchoServer.printSelectionKey(key);
                
                if (key.isWritable() && isWrite) {
                    ByteBuffer buffer = charset.encode("Hello World");
                    socketChannel.write(buffer);
                    isWrite = false;
                }
                
                if (key.isReadable()) {
                    ByteBuffer readBuff = ByteBuffer.allocate(1024);  
                    socketChannel.read(readBuff);  
                    readBuff.flip(); 
                    System.out.println(charset.decode(readBuff).toString());
                    shutdown(socketChannel);
                }
                
            } 
        }
    }

    private static void shutdown(SocketChannel socketChannel) throws IOException {
        socketChannel.close();
        System.exit(0);
    }

}
