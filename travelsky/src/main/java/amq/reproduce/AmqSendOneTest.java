package amq.reproduce;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class AmqSendOneTest {

    public static void main(String[] args) {

        System.out.print("Test Amq send starts.");
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

    private static void sendMessage(Session session, MessageProducer producer) throws JMSException {

        String data = null;
        try {
            File file = new File("queue01-1m.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            data = br.readLine();

        } catch (IOException e) {
            e.printStackTrace();
        }
        
        TextMessage message = session.createTextMessage(data);
        System.out.println("Send message: " + "ActiveMQ 发送的消息");
        producer.send(message);
    }

}
