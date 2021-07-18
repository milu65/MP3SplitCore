package com.AudioSplitter.Controller;

import com.AudioSplitter.Service.HTTPUpload;
import com.AudioSplitter.Service.InstantTransfer;
import com.AudioSplitter.Service.SplitterService;
import com.Task.SplitTaskObject;
import com.Task.TaskIDGenerator;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

@WebServlet(name = "FileUploadServlet", value = "/FileUploadServlet")
public class FileUploadServlet extends HttpServlet {


    public void init() {
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            HTTPUpload hu = new HTTPUpload(req);
            resp.setContentType("text/json");
            FileItem target=hu.getContent("mp3");
            if(target==null){
                resp.getWriter().print("file upload unsuccessful");
                return;
            }

            long taskID=TaskIDGenerator.generate();
            taskID=System.currentTimeMillis();
            File result=new File("C:\\Users\\millby\\Desktop\\upload",taskID+".mp3");
            target.write(result);

            InstantTransfer it=new InstantTransfer();
            it.storeFile(result);

            SplitTaskObject task=new SplitTaskObject(taskID//null pointer error
                    ,hu.getContent("userToken").getString("UTF-8")
                    ,Long.parseLong(hu.getContent("begin").getString("UTF-8"))
                    ,Long.parseLong(hu.getContent("end").getString("UTF-8"))
                    ,result.getAbsolutePath());
            SplitterService ss=new SplitterService();
            ss.split(task);

            resp.getWriter().print("file upload successful. TaskID: "+taskID+"\ndownloadURL: "+ss.getDownloadURL());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        doPost(req,resp);
    }

    @Override
    public void destroy() {

    }
}
