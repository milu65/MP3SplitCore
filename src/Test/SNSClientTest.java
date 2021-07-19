import com.AudioSplitter.Service.AWS.SNSClient;

public class SNSClientTest {

    public static void main(String[] args) {
        String topicArn="arn:aws:sns:us-east-1:969149526624:receipt";
        SNSClient sns=new SNSClient();

        sns.emailUserSubscribe(topicArn,"by985314@dal.ca");
    }
}
