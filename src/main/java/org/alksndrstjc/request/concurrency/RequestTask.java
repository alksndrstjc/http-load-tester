package org.alksndrstjc.request.concurrency;

import org.alksndrstjc.model.ReportModel;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class RequestTask implements Runnable {

    private final HttpClient client;
    private final int numberOfRequests;
    private final HttpRequest request;
    private final ReportModel report;

    public RequestTask(HttpClient client, int numberOfRequests, HttpRequest request, ReportModel report) {
        this.client = client;
        this.numberOfRequests = numberOfRequests;
        this.request = request;
        this.report = report;
    }

    @Override
    public void run() {
        for (int i = 0; i < numberOfRequests; i++) {
            try {
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() != 500) {
                    report.getSuccessCounter().incrementAndGet();
                } else report.getFailureCounter().incrementAndGet();
            } catch (IOException | InterruptedException e) {
                report.getFailureCounter().incrementAndGet();
            }
        }
    }
}
