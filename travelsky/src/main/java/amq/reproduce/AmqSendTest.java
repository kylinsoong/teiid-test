package amq.reproduce;

/**
 * Created by Hanzel on 2017/4/12.
 */
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import org.apache.activemq.ActiveMQConnectionFactory;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class AmqSendTest {
    
    private static final int SEND_NUMBER = 2000;
    public static void main(String[] args) {
        ConnectionFactory connectionFactory;
        Connection connection = null;
        Session session;
        Destination destination;
        MessageProducer producer;
        connectionFactory = new ActiveMQConnectionFactory("admin", "admin", "tcp://localhost:61616");
        try {
            connection = connectionFactory.createConnection();
            connection.start();
            session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
            destination = session.createQueue("FirstQueue");
            producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.PERSISTENT);
//            String data = form1MBData(); // 4MB per message
//            for (int i = 1; i <= SEND_NUMBER; i++){
//                TextMessage message = session.createTextMessage(data);
//                producer.send(message);
//                System.out.println("Send message: " + "ActiveMQ 发送的消息" + i);
//            }
            sendMessage(session, producer);
            
            session.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != connection)
                    connection.close();
            } catch (Throwable ignore) {}
        }
    }

    private static String form1MBData() {
        return new String(ByteBuffer.allocate(1024).array());
    }

    public static void sendMessage(Session session, MessageProducer producer)
            throws Exception {
        String data = null;
        try {
            File file = new File("queue01-1m.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            data = br.readLine();

        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 1; i <= SEND_NUMBER; i++) {
//            TextMessage message = session.createTextMessage("ActiveMQ 发送的消息" + i);
            TextMessage message = session.createTextMessage(data);
//            Thread.sleep(100);
            System.out.println("Send message: " + "ActiveMQ 发送的消息" + i);
            producer.send(message);
        }

    }

}
