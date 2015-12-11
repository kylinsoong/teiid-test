package org.teiid.test.server.netty.nio;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.teiid.test.server.netty.nio.BioEchoServer.Message;

public class BioEchoClient {

	public static void main(String[] args) throws IOException {

		Executor executor = Executors.newCachedThreadPool(new NamedThreadFactory("Client"));
		for(int i = 0 ; i < 10 ; i ++) {
			Sender sender = new Sender();
			executor.execute(sender);
		}
	}
	
	public static interface ISender extends Runnable {
		void write(Message msg) throws Exception;
		Message read() throws Exception;
		void close() throws Exception;
	}
	
	public static class Sender implements ISender {
		
		private Socket socket;
		private ObjectInputStream in;
		private ObjectOutputStream out;
		
		public Sender() throws IOException {
			this.socket = new Socket("localhost", 31000);
		}

		@Override
		public void run() {
			try {
				write(new Message("Hello World"));
				Message msg = read();
				close();
				System.out.println(Thread.currentThread().getName() + ", " + msg);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}

		@Override
		public void write(Message msg) throws Exception {
			out = new ObjectOutputStream(socket.getOutputStream());
			out.writeObject(msg);
			out.flush();
		}

		@Override
		public Message read() throws Exception {
			in = new ObjectInputStream(socket.getInputStream());
			return (Message) in.readObject();
		}

		@Override
		public void close() throws Exception {
			in.close();
			out.close();
			socket.close();
			
		}
		
	}

}
