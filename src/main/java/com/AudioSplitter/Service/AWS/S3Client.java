package com.AudioSplitter.Service.AWS;
import com.Credentials.MyCredentials;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;

import java.io.*;
import java.net.URL;
import java.time.Instant;
import java.util.Date;

public class S3Client {
    public final static String DEFAULT_BUCKET_NAME="testbucket324129384";

    private AmazonS3 s3Client=null;

    public S3Client()throws AmazonS3Exception,AmazonServiceException{
        s3Client=AmazonS3ClientBuilder.standard()
                    .withCredentials(new MyCredentials())
                    .withRegion(Regions.US_EAST_1)
                    .build();
    }

    public boolean doesObjectExist(String bucketName,String objectKey){
        return s3Client.doesObjectExist(bucketName,objectKey);
    }

    private S3Object getObject(String bucketName,String objectKey){
        if(!doesObjectExist(bucketName,objectKey))return null;
        return s3Client.getObject(new GetObjectRequest(bucketName,objectKey));
    }

    public String downloadFile(String bucketName,String objectKey,String targetAddress){
        S3Object obj=getObject(bucketName,objectKey);
        if(obj==null)return null;

        String fileName=objectKey+"."+obj.getObjectMetadata().getContentType();
        fileName=objectKey;
        File outputDir=new File(targetAddress);
        if(!outputDir.exists()){
            outputDir.mkdir();
        }
        File outputFile=new File(targetAddress,fileName);

        InputStream is=obj.getObjectContent();
        OutputStream os=null;
        try {
            os=new FileOutputStream(outputFile);
            byte[] buffer =new byte[1024];
            int size=0;

            while ((size = is.read(buffer)) > 0) {
                os.write(buffer,0,size);
            }

            is.close();
            os.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        return outputFile.getAbsolutePath();
    }

    public boolean uploadFile(String bucketName,String objectKey,String targetAddress){
        File target=new File(targetAddress);
        if(!target.exists())return false;
        PutObjectRequest request=new PutObjectRequest(bucketName,objectKey,target);
        ObjectMetadata metadata=new ObjectMetadata();
        String[] split =target.getName().split("[.]");
        metadata.setContentType(split[split.length-1]);
        request.setMetadata(metadata);
        s3Client.putObject(request);
        return true;
    }

    public String createPublicDownloadAddressExp24h(String bucketName,String objectKey){
        long expTimeMillis=Instant.now().toEpochMilli();
        expTimeMillis+=1000*60*60*24;
        Date expiration=new Date(expTimeMillis);
        return createPublicDownloadAddress(bucketName,objectKey,expiration);
    }

    public String createPublicDownloadAddress(String bucketName, String objectKey, Date expDate){
        GeneratePresignedUrlRequest generatePresignedUrlRequest=
                new GeneratePresignedUrlRequest(bucketName,objectKey).withMethod(HttpMethod.GET)
                        .withExpiration(expDate);
        URL url = s3Client.generatePresignedUrl(generatePresignedUrlRequest);
        return url.toString();
    }
}
