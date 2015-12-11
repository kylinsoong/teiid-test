package org.teiid.test.server.netty.nio;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class BioEchoServer {

	public static void main(String[] args) throws IOException {

		Executor executor = Executors.newCachedThreadPool(new NamedThreadFactory("Bio-Worker"));
		ServerSocket server = new ServerSocket(31000);
		while(true){
			Socket socket = server.accept();
			Worker worker = new Worker(socket);
			executor.execute(worker);
		}
	}
	
	public static class Message implements Serializable {

		private static final long serialVersionUID = 8360649706677753459L;
		
		private String msg;

		public Message(String msg) {
			this.msg = msg;
		}

		@Override
		public String toString() {
			return msg;
		} 
	}
	
	public static interface IWorker extends Runnable {
		Message read() throws Exception;
		void write(Message msg) throws Exception;
		void close() throws Exception;
	}
	
	public static class Worker implements IWorker {	
		private Socket socket;
		private ObjectInputStream in;
		private ObjectOutputStream out;
		public Worker(Socket socket) throws IOException {
			this.socket = socket;
		}
		@Override
		public void run() {
			try {
				Message msg = read();
				String name = Thread.currentThread().getName() + "/" + socket.getRemoteSocketAddress();
				write(new Message("echo(" + name + "):" + msg));
				close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		@Override
		public Message read() throws Exception {
			in = new ObjectInputStream(socket.getInputStream());
			return (Message) in.readObject();
		}
		@Override
		public void write(Message msg) throws IOException {
			out = new ObjectOutputStream(socket.getOutputStream());
			out.writeObject(msg);
			out.flush();
		}
		@Override
		public void close() throws IOException {
			in.close();
			out.close();
			socket.close();
		}	
	}

}
