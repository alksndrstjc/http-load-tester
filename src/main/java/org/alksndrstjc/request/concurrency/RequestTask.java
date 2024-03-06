package org.alksndrstjc.request.concurrency;

import org.alksndrstjc.model.ReportModel;
import org.alksndrstjc.model.RequestStatsThreadSafe;
import org.alksndrstjc.request.RequestHandler;

import java.util.concurrent.atomic.AtomicInteger;

public class RequestTask implements Runnable {

    private final int numberOfRequests;
    private final RequestHandler handler;
    private final ReportModel report;

    public RequestTask(int numberOfRequests, RequestHandler handler, ReportModel report) {
        this.numberOfRequests = numberOfRequests;
        this.handler = handler;
        this.report = report;

    }

    @Override
    public void run() {
        String url = handler.getRequest().uri().toString();

        for (int i = 0; i < numberOfRequests; i++) {
            try {
                RequestHandler.RequiredData data = handler.doRequest();

                SharedStatsRPS.requestTimestamps.put(data.endTimeStamp);
                RequestStatsThreadSafe.addStats(url, data.requestStats);

                report.getPerUrlSuccessCounter().computeIfAbsent(url, k -> new AtomicInteger());
                report.getPerUrlFailureCounter().computeIfAbsent(url, k -> new AtomicInteger());

                if (data.code != 500) {
                    report.getPerUrlSuccessCounter().get(url).incrementAndGet();
                } else report.getPerUrlFailureCounter().get(url).incrementAndGet();

            } catch (InterruptedException e) {
                report.getPerUrlFailureCounter().get(url).incrementAndGet();

            } finally {
                if (report.getPerUrlFailureCounter().get(url).get() + report.getPerUrlSuccessCounter().get(url).get() == report.getTotalRequests()) {
                    SharedStatsRPS.TERMINATE_RPS.set(true);
                }
            }
        }
    }
}
