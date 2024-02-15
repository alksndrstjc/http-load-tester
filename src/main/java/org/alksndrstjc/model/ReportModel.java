package org.alksndrstjc.model;

import lombok.Getter;

import java.util.concurrent.atomic.AtomicInteger;

@Getter
public class ReportModel {
    private final int totalRequests;
    private final AtomicInteger successCounter = new AtomicInteger();
    private final AtomicInteger failureCounter = new AtomicInteger();

    public ReportModel(int totalRequests) {
        this.totalRequests = totalRequests;
    }
}
