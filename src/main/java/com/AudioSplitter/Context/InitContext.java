package com.AudioSplitter.Context;

import com.AudioSplitter.Service.SplitterService;
import com.AudioSplitter.Task.SplitTaskObject;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class InitContext implements ServletContextListener {
    public static ServletContext servletContext=null;

    public void contextInitialized(ServletContextEvent sce) {
        ServletContext sc=sce.getServletContext();
        servletContext=sc;
        LinkedBlockingQueue<SplitTaskObject> taskQueue=new LinkedBlockingQueue<>();
        Map<String,String>map=new ConcurrentHashMap<>();
        sc.setAttribute("taskQueue",taskQueue);
        sc.setAttribute("resultMap",map);
        Thread t=new Thread(new SplitterService(taskQueue));
        t.start();
        System.out.println("InitContext run");
    }

    public void contextDestroyed(ServletContextEvent sce) {

    }
}
