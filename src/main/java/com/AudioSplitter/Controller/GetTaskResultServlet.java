package com.AudioSplitter.Controller;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet(name = "GetTaskResultServlet", value = "/GetTaskResultServlet")
public class GetTaskResultServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Map<String,String> resultMap
                =(Map<String,String>)req.getServletContext().getAttribute("resultMap");
        String taskID=req.getParameter("taskID");
        resp.getWriter().print(resultMap.getOrDefault(taskID,"no result"));
    }
}
