package org.alksndrstjc.request;

import org.alksndrstjc.model.ReportModel;
import org.alksndrstjc.request.concurrency.RequestTask;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.concurrent.ExecutorService;

public class RequestHandler {

    private final ExecutorService executorService;
    private final HttpClient client;

    public RequestHandler(ExecutorService executorService, HttpClient client) {
        this.executorService = executorService;
        this.client = client;
    }

    public void handleRequest(HttpRequest request,
                              int numberOfSubmission,
                              int numberOfRequests,
                              ReportModel reportModel) {
        for (int i = 0; i < numberOfSubmission; i++)
            executorService.submit(new RequestTask(
                            client,
                            numberOfRequests,
                            request,
                            reportModel.getSuccessCounter(),
                            reportModel.getFailureCounter()
                    )
            );
    }
}
