package com.AudioSplitter.Service;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MultipartContent {

    private Map<String,FileItem> content=new HashMap<>();

    public final static String userUploadDir="/tmp/mp3Splitter/upload";
    public MultipartContent(HttpServletRequest req) throws FileUploadException, UnsupportedEncodingException {
        File uploadDir=new File(userUploadDir);
        if(!uploadDir.exists()){
            uploadDir.mkdirs();
        }
        if(ServletFileUpload.isMultipartContent(req)){
            ServletFileUpload upload=new ServletFileUpload(new DiskFileItemFactory(10240,uploadDir));
            List<FileItem> fileItems= upload.parseRequest(req);
            for(FileItem item:fileItems){
                content.put(item.getFieldName(),item);
            }
        }
    }

    public FileItem getContent(String name){
        return content.getOrDefault(name,null);
    }
}
