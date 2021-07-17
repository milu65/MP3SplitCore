package com.AudioSplitter.Controller;

import com.AudioSplitter.Service.MP3Splitter;

import java.io.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet {
    private String message;
    private static String path="F:\\git\\GitHub\\MP3SplitCore\\testMp3\\";

    public void init() {
        message = "Hello World!";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String fileName=request.getParameter("name");
        MP3Splitter splitter= new MP3Splitter(new File(path+fileName));
        splitter.subsequence(10000,20000,new File("/tmp/splitterOutput"));

        response.setContentType("text/html");

        // Hello
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>" + message + "</h1>");
        out.println("</body></html>");
    }

    public void destroy() {
    }
}