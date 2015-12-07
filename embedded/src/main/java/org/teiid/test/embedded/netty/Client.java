package org.teiid.test.embedded.netty;

import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.ResultSet;

import org.teiid.client.DQP;
import org.teiid.client.RequestMessage;
import org.teiid.client.RequestMessage.ResultsMode;
import org.teiid.net.socket.Message;
import org.teiid.net.socket.ServiceInvocationStruct;
import org.teiid.netty.handler.codec.serialization.ObjectEncoderOutputStream;

public class Client {

    @SuppressWarnings("resource")
    public static void main(String[] args) throws UnknownHostException, IOException {

        RequestMessage reqMsg = new RequestMessage();
        reqMsg.setCommands(new String[]{"SELECT * FROM Product"});
        reqMsg.setBatchedUpdate(false);
        reqMsg.setResultsMode(ResultsMode.EITHER);
        reqMsg.setReturnAutoGeneratedKeys(false);
        reqMsg.setDelaySerialization(true);
        reqMsg.setCursorType(ResultSet.TYPE_FORWARD_ONLY);
        reqMsg.setFetchSize(2048);
        reqMsg.setRowLimit(0);
        reqMsg.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        reqMsg.setSync(false);
        reqMsg.setPartialResults(false);
        reqMsg.setValidationMode(false);
        reqMsg.setUseResultSetCache(false);
        reqMsg.setAnsiQuotedIdentifiers(true);
        reqMsg.setExecutionId(100);
        
        Class<?> targetClass = DQP.class;
        Method method = getExecuteRequest();
        
        Message message = new Message();
        message.setContents(new ServiceInvocationStruct(new Object[]{100, reqMsg}, method.getName(), targetClass));
        message.setMessageKey(Integer.valueOf(27));
        
        Socket socket = new Socket("localhost", 31000);
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        ObjectEncoderOutputStream outputStream = new ObjectEncoderOutputStream(out, 1<<15);
        outputStream.writeObject(message);
        outputStream.flush();     
        outputStream.reset();
    }

    private static Method getExecuteRequest() {
        for(Method m : DQP.class.getMethods()){
            if(m.getName().equals("executeRequest"))
            return m;
        }
        return null;
    }

}
