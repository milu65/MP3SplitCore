package com.AudioSplitter.Controller;

import com.AudioSplitter.Service.AWS.SNSClient;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "EmailSubscribeServlet", value = "/EmailSubscribeServlet")
public class EmailSubscribeServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String email=req.getParameter("email");
        SNSClient sns=new SNSClient();
        String topicArn="arn:aws:sns:us-east-1:969149526624:receipt";
        sns.emailUserSubscribe(topicArn,email);
        resp.getWriter().print("confirm email sent");
    }
}
