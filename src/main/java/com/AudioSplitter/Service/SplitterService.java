package com.AudioSplitter.Service;

import com.Task.SplitTaskObject;
import com.alibaba.fastjson.JSONObject;

import javax.jms.JMSException;
import java.io.File;
import java.io.IOException;

public class SplitterService {
//    public final File outputDir=new File("/tmp/SplitterResult");
    public final String downloadDir="F:\\git\\GitHub\\MP3SplitCore\\testMp3\\downloaded";
    public final File outputDir=new File("F:\\git\\GitHub\\MP3SplitCore\\testMp3\\output");

    private final String bucketName="testbucket324129384";

    public void SplitterService(){

    }

    public void split(SplitTaskObject task) throws IOException, JMSException {
        String targetFileDir=task.getRef();
        S3Client s3=new S3Client();

        //download file if using instant transfer
        if(!task.isLocalFile()){//assert the file exists on S3
            targetFileDir=s3.downloadFile(bucketName,task.getRef(),downloadDir);
            System.out.println("downloaded file from s3");
        }

        //split mp3
        MP3Splitter splitter=new MP3Splitter(new File(targetFileDir));
        String resultFile= splitter.subsequence(task.getBegin(),task.getEnd(),outputDir);
        System.out.println("split finished");

        //upload file to S3 and generate a download URL
        String fileName=task.getId()+".mp3";
        s3.uploadFile(bucketName,fileName,resultFile);
        String downloadURL=s3.createPublicDownloadAddressExp24h(bucketName,fileName);
        System.out.println("S3 uploaded");

        //forward result to SQS
        SQSClient sqs=new SQSClient();
        JSONObject object=new JSONObject();
        object.put("id",task.getId());
        object.put("userToken",task.getUserToken());
        object.put("resultFile",downloadURL);
        sqs.sendMessage(object.toJSONString());
        sqs.close();

        System.out.println(downloadURL);
    }
}
