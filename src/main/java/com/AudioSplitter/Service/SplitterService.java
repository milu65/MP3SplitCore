package com.AudioSplitter.Service;

import com.Task.SplitTaskObject;
import com.alibaba.fastjson.JSONObject;

import javax.jms.JMSException;
import java.io.File;
import java.io.IOException;

public class SplitterService {
//    public final File outputDir=new File("/tmp/SplitterResult");
    public final File outputDir=new File("F:\\git\\GitHub\\MP3SplitCore\\testMp3\\output");

    private final String bucketName="testbucket324129384";

    private String downloadURL="";

    public void SplitterService(){

    }

    public void split(SplitTaskObject task) throws IOException, JMSException {
        //split mp3
        String targetFileDir=task.getRef();
        MP3Splitter splitter=new MP3Splitter(new File(targetFileDir));
        String resultFile= splitter.subsequence(task.getBegin(),task.getEnd(),outputDir);
        System.out.println("split finished");

        //upload file to S3 and generate a download URL
        String downloadURL= storeResultFileAndCreateDownloadURL(task,resultFile);
        System.out.println("S3 uploaded");

        //forward result to SQS
        forwardResultToSQS(task,downloadURL);

        System.out.println("taskID:"+task.getId()+"\ndownloadAddress:\n"+downloadURL);
    }

    private String storeResultFileAndCreateDownloadURL(SplitTaskObject task,String resultFile){
        S3Client s3=new S3Client();
        String fileName=task.getId()+".mp3";
        s3.uploadFile(bucketName,fileName,resultFile);
        this.downloadURL=s3.createPublicDownloadAddressExp24h(bucketName,fileName);
        return this.downloadURL;
    }

    private void forwardResultToSQS(SplitTaskObject task,String downloadURL) throws JMSException {
        SQSClient sqs=new SQSClient();
        JSONObject object=new JSONObject();
        object.put("id",task.getId());
        object.put("userToken",task.getUserToken());
        object.put("resultFile",downloadURL);
        sqs.sendMessage(object.toJSONString());
        sqs.close();
    }

    public String getDownloadURL(){
        return downloadURL;
    }
}
