package com.AudioSplitter.Service;

import com.Task.SplitTaskObject;

import java.io.File;

public class InstantTransfer {

    private final String downloadDir="F:\\git\\GitHub\\MP3SplitCore\\testMp3\\downloaded";

    public String generateFileHash(File file){
        return "";
    }

    public void storeFile(File file){

    }

    public String hashToFile(String hash){
        S3Client client=new S3Client();
        String objKey=hash+".mp3";
        if(!client.doesObjectExist(S3Client.DEFAULT_BUCKET_NAME,objKey)){
            return null;
        }
        return client.downloadFile(S3Client.DEFAULT_BUCKET_NAME,objKey,downloadDir);
    }
}
