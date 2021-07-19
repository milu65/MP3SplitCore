package com.AudioSplitter.Service.AWS;
import com.AudioSplitter.Service.AWS.Credentials.MyCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.SubscribeRequest;

public class SNSClient {
    private AmazonSNS snsClient;

    public SNSClient(){
        snsClient= AmazonSNSClientBuilder.standard()
                .withRegion(Regions.US_EAST_1)
                .withCredentials(new MyCredentials())
                .build();
    }

    public String emailUserSubscribe(String topicArn, String userEmail){

        SubscribeRequest subscribeRequest = new SubscribeRequest(topicArn, "email", userEmail);
        System.out.println(snsClient.listTopics());
        snsClient.subscribe(subscribeRequest);

        return snsClient.getCachedResponseMetadata(subscribeRequest).getRequestId();
    }
}
