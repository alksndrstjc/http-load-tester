package org.alksndrstjc.model;

import lombok.Builder;
import org.alksndrstjc.request.concurrency.SharedStatsRPS;

import static org.alksndrstjc.model.RequestStatsThreadSafe.*;

@Builder
public class FinalReportModel {
    private String requestUrl;
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

    public static FinalReportModel init(String requestUrl, ReportModel successCountReport) {
        return FinalReportModel.builder()
                .requestUrl(requestUrl)
                .successfulRequests(successCountReport.getPerUrlSuccessCounter().get(requestUrl).get())
                .failedRequests(successCountReport.getPerUrlFailureCounter().get(requestUrl).get())
                .rps(SharedStatsRPS.latestRps)
                .totalRequestTimeMin(getRequestedItemMin(requestUrl, RequestedItem.TOTAL_TIME))
                .totalRequestTimeMax(getRequestedItemMax(requestUrl, RequestedItem.TOTAL_TIME))
                .totalRequestTimeAvrg(getRequestedItemAvrg(requestUrl, RequestedItem.TOTAL_TIME))
                .ttfbMin(getRequestedItemMin(requestUrl, RequestedItem.TTFB))
                .ttfbMax(getRequestedItemMax(requestUrl, RequestedItem.TTFB))
                .ttfbAvrg(getRequestedItemAvrg(requestUrl, RequestedItem.TTFB))
                .ttlbMin(getRequestedItemMin(requestUrl, RequestedItem.TTLB))
                .ttlbMax(getRequestedItemMax(requestUrl, RequestedItem.TTLB))
                .ttlbAvrg(getRequestedItemAvrg(requestUrl, RequestedItem.TTLB))
                .build();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Results:\n");
        sb.append("Request url: ").append(requestUrl).append("\n");
        sb.append(String.format(" Total Requests  (2XX)...................: %d\n", successfulRequests));
        sb.append(String.format(" Failed Requests (5XX)...................: %d\n", failedRequests));
        sb.append(String.format(" Request/second..........................: %.2f\n\n", rps));
        sb.append(String.format("Total Request Time (s) (Min, Max, Mean).......: %.2f, %.2f, %.2f\n", totalRequestTimeMin, totalRequestTimeMax, totalRequestTimeAvrg));
        sb.append(String.format("Time To First Byte (s) (Min, Max, Mean).......: %.2f, %.2f, %.2f\n", ttfbMin, ttfbMax, ttfbAvrg));
        sb.append(String.format("Time to Last Byte  (s) (Min, Max, Mean).......: %.2f, %.2f, %.2f", ttlbMin, ttlbMax, ttlbAvrg));
        sb.append("\n");
        return sb.toString();
    }


}
