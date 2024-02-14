package org.alksndrstjc.model;

import lombok.Builder;
import org.alksndrstjc.request.concurrency.SharedStatsRPS;

import static org.alksndrstjc.model.RequestStatsThreadSafe.*;

@Builder
public class FinalReportModel {
    private int successfulRequests;
    private int failedRequests;
    private double rps;
    private double totalRequestTimeMin;
    private double totalRequestTimeMax;
    private double totalRequestTimeAvrg;
    private double ttfbMin;
    private double ttfbMax;
    private double ttfbAvrg;
    private double ttlbMin;
    private double ttlbMax;
    private double ttlbAvrg;

    public static FinalReportModel init(ReportModel successCountReport) {
        return FinalReportModel.builder()
                .successfulRequests(successCountReport.getSuccessCounter().get())
                .failedRequests(successCountReport.getFailureCounter().get())
                .rps(SharedStatsRPS.latestRps)
                .totalRequestTimeMin(getRequestedItemMin(RequestedItem.TOTAL_TIME).getAsDouble())
                .totalRequestTimeMax(getRequestedItemMax(RequestedItem.TOTAL_TIME).getAsDouble())
                .totalRequestTimeAvrg(getRequestedItemAvrg(RequestedItem.TOTAL_TIME).getAsDouble())
                .ttfbMin(getRequestedItemMin(RequestedItem.TTFB).getAsDouble())
                .ttfbMax(getRequestedItemMax(RequestedItem.TTFB).getAsDouble())
                .ttfbAvrg(getRequestedItemAvrg(RequestedItem.TTFB).getAsDouble())
                .ttlbMin(getRequestedItemMin(RequestedItem.TTLB).getAsDouble())
                .ttlbMax(getRequestedItemMax(RequestedItem.TTLB).getAsDouble())
                .ttlbAvrg(getRequestedItemAvrg(RequestedItem.TTLB).getAsDouble())
                .build();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Results:\n");
        sb.append(String.format(" Total Requests  (2XX)...................: %d\n", successfulRequests));
        sb.append(String.format(" Failed Requests (5XX)...................: %d\n", failedRequests));
        sb.append(String.format(" Request/second..........................: %.2f\n\n", rps));
        sb.append(String.format("Total Request Time (s) (Min, Max, Mean).......: %.2f, %.2f, %.2f\n", totalRequestTimeMin, totalRequestTimeMax, totalRequestTimeAvrg));
        sb.append(String.format("Time To First Byte (s) (Min, Max, Mean).......: %.2f, %.2f, %.2f\n", ttfbMin, ttfbMax, ttfbAvrg));
        sb.append(String.format("Time to Last Byte  (s) (Min, Max, Mean).......: %.2f, %.2f, %.2f", ttlbMin, ttlbMax, ttlbAvrg));
        return sb.toString();
    }


}
