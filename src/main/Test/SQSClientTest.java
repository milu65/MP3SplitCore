import com.AudioSplitter.Service.SQSClient;

import javax.jms.JMSException;

public class SQSClientTest {
    public static void main(String[] args) throws JMSException {
        SQSClient client=new SQSClient();
        client.sendMessage("SQSClient test");
        client.close();
    }
}
