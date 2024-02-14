package org.alksndrstjc.request.concurrency;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class SharedStatsRPS {
    public static final AtomicBoolean TERMINATE_RPS = new AtomicBoolean(false);
    public static final AtomicInteger REQUEST_COUNTER = new AtomicInteger();
    public static int latestRps = 0;

    public static void setLatestRPS() {
        latestRps = REQUEST_COUNTER.getAndSet(0);
    }
}
