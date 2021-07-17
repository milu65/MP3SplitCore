package com.AudioSplitter.Service;

import com.Credentials.MyCredentials;
import com.amazon.sqs.javamessaging.ProviderConfiguration;
import com.amazon.sqs.javamessaging.SQSConnection;
import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;

import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

public class SQSClient {
    public static void main(String[] args) throws JMSException {
        SQSClient client=new SQSClient();
        client.sendMessage("SQSClient test");
        client.close();
    }

    private final String QUEUE_NAME ="FinishedTasks.fifo";

    private Session session=null;
    private MessageProducer producer=null;

    public SQSClient() throws JMSException {
        SQSConnectionFactory connectionFactory=new SQSConnectionFactory(
                new ProviderConfiguration(),
                AmazonSQSClientBuilder.standard()
                        .withRegion(Regions.US_EAST_1)
                        .withCredentials(new MyCredentials())
        );

        SQSConnection connection=connectionFactory.createConnection();
        session=connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
        producer=session.createProducer(session.createQueue(QUEUE_NAME));
    }

    public void sendMessage(String json) throws JMSException {
        TextMessage message = session.createTextMessage(json);
        message.setStringProperty("JMSXGroupID","Default");
        message.setStringProperty("JMS_SQS_DeduplicationId", "1");
        producer.send(message);
    }

    public void close() throws JMSException {
        session.close();
    }
}
