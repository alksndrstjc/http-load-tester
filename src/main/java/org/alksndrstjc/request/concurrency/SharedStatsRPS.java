package org.alksndrstjc.request.concurrency;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class SharedStatsRPS {
    public static final AtomicBoolean TERMINATE_RPS = new AtomicBoolean(false);
    public static final BlockingQueue<Long> requestTimestamps = new LinkedBlockingQueue<>();
    public static int latestRps = 0;
}
