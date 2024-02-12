package org.alksndrstjc.request.concurrency;

import org.alksndrstjc.model.ReportModel;
import org.alksndrstjc.model.RequestStats;
import org.alksndrstjc.model.RequestStatsThreadSafe;
import org.alksndrstjc.utils.DecimalFormatter;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class RequestTask implements Runnable {

    private final HttpClient client;
    private final int numberOfRequests;
    private final HttpRequest request;
    private final ReportModel report;

    private final AtomicInteger requestCounter;
    private final AtomicBoolean terminateRequestPerMinute;

    public RequestTask(HttpClient client, int numberOfRequests, HttpRequest request, ReportModel report,
                       AtomicInteger requestCounter,
                       AtomicBoolean terminateRequestPerMinute) {
        this.client = client;
        this.numberOfRequests = numberOfRequests;
        this.request = request;
        this.report = report;
        this.requestCounter = requestCounter;
        this.terminateRequestPerMinute = terminateRequestPerMinute;
    }

    @Override
    public void run() {
        for (int i = 0; i < numberOfRequests; i++) {
            try {
                long startTime = System.currentTimeMillis();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                long endTime = System.currentTimeMillis();
                long totalTime = endTime - startTime;

                requestCounter.incrementAndGet();

                HttpHeaders headers = response.headers();
                List<String> dateHeaderValues = headers.allValues("Date");
                long firstByteTime = 0;
                long lastByteTime = 0;

                //todo: using date header is imprecise if it doesn't include milliseconds
                // rework using lower level libraries that actually read bytes
                if (!dateHeaderValues.isEmpty()) {
                    Instant startInstant = Instant.ofEpochMilli(startTime);
                    Instant firstByteInstant = Instant.from(DateTimeFormatter.RFC_1123_DATE_TIME.parse(dateHeaderValues.getFirst()));
                    firstByteTime = Duration.between(startInstant, firstByteInstant).toMillis();
                    Instant endInstant = Instant.ofEpochMilli(endTime);
                    lastByteTime = Duration.between(firstByteInstant, endInstant).toMillis();
                }

                RequestStatsThreadSafe.addStats(new RequestStats(
                        DecimalFormatter.format((double) totalTime / 1000),
                        DecimalFormatter.format((double) firstByteTime / 1000),
                        DecimalFormatter.format((double) lastByteTime / 1000)));

                if (response.statusCode() != 500) {
                    report.getSuccessCounter().incrementAndGet();
                } else report.getFailureCounter().incrementAndGet();

                if (report.getFailureCounter().get() + report.getSuccessCounter().get() == numberOfRequests) {
                    terminateRequestPerMinute.set(true);
                }
            } catch (IOException | InterruptedException e) {
                report.getFailureCounter().incrementAndGet();

                if (report.getFailureCounter().get() + report.getSuccessCounter().get() == numberOfRequests) {
                    terminateRequestPerMinute.set(true);
                }
            }
        }
    }
}