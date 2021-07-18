package com.Task;

import java.util.concurrent.atomic.AtomicLong;

public class TaskIDGenerator {
    private static AtomicLong count= new AtomicLong(0);
    public static long generate(){
        return count.addAndGet(1);
    }
}
