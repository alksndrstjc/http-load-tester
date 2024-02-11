package org.alksndrstjc.request.concurrency;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorsServiceFactory {
    public ExecutorService createFixedThreadPool(int numOfThreads) {
        return Executors.newFixedThreadPool(numOfThreads);
    }
}
