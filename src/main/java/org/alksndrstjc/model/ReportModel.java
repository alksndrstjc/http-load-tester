package org.alksndrstjc.model;

import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
public class ReportModel {
    private final int totalRequests;
    private final Map<String, AtomicInteger> perUrlSuccessCounter = new ConcurrentHashMap<>();
    private final Map<String, AtomicInteger> perUrlFailureCounter = new ConcurrentHashMap<>();

    public ReportModel(int totalRequests) {
        this.totalRequests = totalRequests;
    }
}
