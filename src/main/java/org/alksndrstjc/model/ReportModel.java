package org.alksndrstjc.model;

import java.util.concurrent.atomic.AtomicInteger;

public class ReportModel {

    private final AtomicInteger successCounter = new AtomicInteger();
    private final AtomicInteger failureCounter = new AtomicInteger();


    public AtomicInteger getSuccessCounter() {
        return successCounter;
    }

    public AtomicInteger getFailureCounter() {
        return failureCounter;
    }
}
