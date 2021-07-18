package com.AudioSplitter.Controller;

import com.AudioSplitter.Service.InstantTransfer;
import com.AudioSplitter.Service.SplitterService;
import com.Task.SplitTaskObject;
import com.Task.TaskIDGenerator;

import javax.jms.JMSException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet(name = "HashUploadServlet", value = "/HashUploadServlet")
public class HashUploadServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String hash=req.getParameter("hash");
        long taskID= TaskIDGenerator.generate();
        taskID=System.currentTimeMillis();
        InstantTransfer it=new InstantTransfer();
        String dir=it.hashToFile(hash);
        SplitTaskObject task=new SplitTaskObject(taskID
                ,req.getParameter("userToken")
                ,Long.parseLong(req.getParameter("begin"))
                ,Long.parseLong(req.getParameter("end"))
                ,dir);


        SplitterService ss=new SplitterService();
        try {
            ss.split(task);
        } catch (JMSException e) {
            e.printStackTrace();
        }

        resp.getWriter().print("file upload successful. TaskID: "+taskID+"\ndownloadURL: "+ss.getDownloadURL());
    }
}
