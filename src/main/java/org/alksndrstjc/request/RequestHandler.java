package org.alksndrstjc.request;

import org.alksndrstjc.model.ReportModel;
import org.alksndrstjc.request.concurrency.RequestTask;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class RequestHandler {

    private final ExecutorService executor;
    private final HttpClient client;


    public RequestHandler(ExecutorService executorService, HttpClient client) {
        this.executor = executorService;
        this.client = client;
    }

    public void handleRequest(HttpRequest request, int numberOfRequests, int numberOfThreads, ReportModel report,
                              AtomicInteger requestCounter,
                              AtomicBoolean terminateRps) {
        if (numberOfRequests < 0 || numberOfThreads < 0)
            throw new IllegalArgumentException("Negative numOfRequests or numOfThreads.");

        int batchSize = (int) Math.round((double) numberOfRequests / (double) numberOfThreads);

        if (batchSize != 0) {
            int remainingRequests = numberOfRequests - (batchSize * numberOfThreads);

            //check if all requests can be processed in batches in fixed number of threads
            if (remainingRequests == 0) {
                for (int i = 1; i <= numberOfRequests; i += batchSize) {
                    submit(batchSize, request, report,
                            requestCounter,
                            terminateRps);
                }
            } else {
                // submit in batches until last thread
                for (int i = 0; i < numberOfThreads - 1; i++) {
                    submit(batchSize, request, report,
                            requestCounter,
                            terminateRps);
                }
                // reserve the last thread for remaining requests + batch size
                submit(remainingRequests + batchSize, request, report,
                        requestCounter,
                        terminateRps);
            }
        } else {
            // check if there are requests which can be processed in a single thread
            if (numberOfRequests != 0) submit(numberOfRequests, request, report,
                    requestCounter,
                    terminateRps);
        }
    }

    private void submit(int requestsNum, HttpRequest request, ReportModel report,
                        AtomicInteger requestCounter,
                        AtomicBoolean terminateRPS) {
        executor.submit(new RequestTask(
                client,
                requestsNum,
                request,
                report,
                requestCounter,
                terminateRPS
        ));
    }
}
