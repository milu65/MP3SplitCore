package com.AudioSplitter.Controller;

import com.AudioSplitter.Service.InstantTransferService;
import com.AudioSplitter.Task.SplitTaskObject;
import com.AudioSplitter.Task.TaskIDGenerator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;


@WebServlet(name = "HashUploadServlet", value = "/HashUploadServlet")
public class HashUploadServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String hash=req.getParameter("hash");
        long taskID= TaskIDGenerator.generate();
        taskID=System.currentTimeMillis();

        InstantTransferService it=new InstantTransferService();
        String dir=it.hashToFile(hash);

        SplitTaskObject task=new SplitTaskObject(taskID
                ,req.getParameter("userToken")
                ,Long.parseLong(req.getParameter("begin"))
                ,Long.parseLong(req.getParameter("end"))
                ,hash);


        LinkedBlockingQueue<SplitTaskObject> taskQueue
                =(LinkedBlockingQueue<SplitTaskObject>)req.getServletContext().getAttribute("taskQueue");
        taskQueue.add(task);

        resp.getWriter().print("file upload successful. TaskID: "+taskID);
    }
}
