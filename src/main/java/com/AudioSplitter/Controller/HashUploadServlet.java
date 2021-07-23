package com.AudioSplitter.Controller;

import com.AudioSplitter.Service.AWS.S3Client;
import com.AudioSplitter.Service.InstantTransferService;
import com.AudioSplitter.Task.SplitTaskObject;
import com.AudioSplitter.Task.TaskIDGenerator;
import com.alibaba.fastjson.JSONObject;

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
        JSONObject responseJson=new JSONObject();


        System.out.println("finding hash: "+hash+".mp3");
        if(!S3Client.doesObjectExist(S3Client.DEFAULT_BUCKET_NAME,hash+".mp3")){
            responseJson.put("state","failed");
            resp.getWriter().print(responseJson.toJSONString());
            return;
        }

        SplitTaskObject task=new SplitTaskObject(taskID
                ,req.getParameter("userToken")
                ,Long.parseLong(req.getParameter("begin"))
                ,Long.parseLong(req.getParameter("end"))
                ,hash
                ,false);


        LinkedBlockingQueue<SplitTaskObject> taskQueue
                =(LinkedBlockingQueue<SplitTaskObject>)req.getServletContext().getAttribute("taskQueue");
        taskQueue.add(task);

        responseJson.put("state","succeeded");
        responseJson.put("id",taskID);
        resp.getWriter().print(responseJson.toJSONString());
    }
}
