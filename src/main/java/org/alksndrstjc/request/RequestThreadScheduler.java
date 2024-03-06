package org.alksndrstjc.request;

import org.alksndrstjc.model.ReportModel;
import org.alksndrstjc.request.concurrency.RequestTask;

import java.util.concurrent.ExecutorService;

public class RequestThreadScheduler {

    private final ExecutorService executor;


    public RequestThreadScheduler(ExecutorService executorService) {
        this.executor = executorService;
    }

    public void scheduleRequest(RequestHandler handler, int numberOfRequests, int numberOfThreads, ReportModel report) {
        if (numberOfRequests < 0 || numberOfThreads < 0)
            throw new IllegalArgumentException("Negative numOfRequests or numOfThreads.");

        int batchSize = (int) Math.round((double) numberOfRequests / (double) numberOfThreads);

        if (batchSize != 0) {
            int remainingRequests = numberOfRequests - (batchSize * numberOfThreads);

            //check if all requests can be processed in batches in fixed number of threads
            if (remainingRequests == 0) {
                for (int i = 1; i <= numberOfRequests; i += batchSize) {
                    submit(batchSize, handler, report);
                }
            } else {
                // submit in batches until last thread
                for (int i = 0; i < numberOfThreads - 1; i++) {
                    submit(batchSize, handler, report);
                }
                // reserve the last thread for remaining requests + batch size
                submit(remainingRequests + batchSize, handler, report);
            }
        } else {
            // check if there are requests which can be processed in a single thread
            if (numberOfRequests != 0) submit(numberOfRequests, handler, report);
        }
    }

    private void submit(int requestsNum, RequestHandler handler, ReportModel report) {
        executor.submit(new RequestTask(
                requestsNum,
                handler,
                report
        ));
    }
}
