package com.AudioSplitter.Service;

import com.AudioSplitter.Service.AWS.S3Client;
import com.amazonaws.util.Md5Utils;

import java.io.File;
import java.io.IOException;

public class InstantTransferService {

    private final String downloadDir="F:\\git\\GitHub\\MP3SplitCore\\testMp3\\downloaded";

    private static final String[] hexDigits = {"0", "1", "2", "3", "4", "5","6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};



    public String generateFileHash(File file) throws IOException {
        byte[]bytes=Md5Utils.computeMD5Hash(file);
        return byteArrayToHexString(bytes);
    }

    private static String byteArrayToHexString(byte[] b) {
        StringBuilder sb = new StringBuilder();
        for (byte i:b){
            sb.append(byteToHexString(i));
        }
        return sb.toString();
    }

    private static String byteToHexString(byte b) {
        int n=b;
        if (n<0)n+=256;
        return hexDigits[n/16]+hexDigits[n%16]; //higher 4 bits and lower 4 bits
    }

    public boolean storeFile(File file){
        String hash="";
        try{
            hash=generateFileHash(file);
        }catch(IOException e){
            return false;
        }
        String fileName=hash+".mp3";
        return S3Client.uploadFile(S3Client.DEFAULT_BUCKET_NAME,fileName,file.getAbsolutePath());
    }

    public String hashToFile(String hash){
        String objKey=hash+".mp3";
        if(!S3Client.doesObjectExist(S3Client.DEFAULT_BUCKET_NAME,objKey)){
            return null;
        }
        return S3Client.downloadFile(S3Client.DEFAULT_BUCKET_NAME,objKey,downloadDir);
    }
}
