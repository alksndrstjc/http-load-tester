package org.alksndrstjc.request.concurrency;

import java.util.concurrent.BlockingQueue;

public class RPSThread extends Thread {

    @Override
    public void run() {
        while (!SharedStatsRPS.TERMINATE_RPS.get()) {
            try {
                Thread.sleep(1000);
                int counter = 0;
                long currentTime = System.currentTimeMillis();
                BlockingQueue<Long> requestTimestamps = SharedStatsRPS.requestTimestamps;
                while (!requestTimestamps.isEmpty()) {
                    Long timestamp = requestTimestamps.poll();
                    if (currentTime - timestamp <= 1000) counter++;
                }
                SharedStatsRPS.latestRps = counter;
            } catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void start() {
        this.setDaemon(true);
        super.start();
    }
}
