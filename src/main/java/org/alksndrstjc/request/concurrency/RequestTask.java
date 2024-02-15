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
import java.util.concurrent.atomic.AtomicInteger;

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
        String url = request.uri().toString();

        for (int i = 0; i < numberOfRequests; i++) {
            try {
                long startTime = System.currentTimeMillis();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                long endTime = System.currentTimeMillis();
                long totalTime = endTime - startTime;

                SharedStatsRPS.requestTimestamps.put(endTime);

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

                RequestStatsThreadSafe.addStats(url, new RequestStats(
                        DecimalFormatter.format((double) totalTime / 1000),
                        DecimalFormatter.format((double) firstByteTime / 1000),
                        DecimalFormatter.format((double) lastByteTime / 1000)));

                report.getPerUrlSuccessCounter().computeIfAbsent(url, k -> new AtomicInteger());
                report.getPerUrlFailureCounter().computeIfAbsent(url, k -> new AtomicInteger());

                if (response.statusCode() != 500) {
                    report.getPerUrlSuccessCounter().get(url).incrementAndGet();
                } else report.getPerUrlFailureCounter().get(url).incrementAndGet();

            } catch (IOException | InterruptedException e) {
                report.getPerUrlFailureCounter().get(url).incrementAndGet();

            } finally {
                if (report.getPerUrlFailureCounter().get(url).get() + report.getPerUrlSuccessCounter().get(url).get() == report.getTotalRequests()) {
                    SharedStatsRPS.TERMINATE_RPS.set(true);
                }
            }
        }
    }
}
