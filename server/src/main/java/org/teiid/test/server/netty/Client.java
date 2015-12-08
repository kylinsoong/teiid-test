package org.teiid.test.server.netty;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import org.teiid.net.socket.Message;
import org.teiid.netty.handler.codec.serialization.ObjectEncoderOutputStream;

public class Client {

	@SuppressWarnings("resource")
	public static void main(String[] args) throws UnknownHostException, IOException {
		
		Message message = new Message();
		message.setContents(new String("Hello Wolrd"));
		message.setMessageKey(1);

		Socket socket = new Socket("localhost", 31001);
		DataOutputStream out = new DataOutputStream(socket.getOutputStream());
		ObjectEncoderOutputStream outputStream = new ObjectEncoderOutputStream(out, 1<<15);
		outputStream.writeObject(message);
        outputStream.flush();
        outputStream.reset();
		socket.close();
	}

}
