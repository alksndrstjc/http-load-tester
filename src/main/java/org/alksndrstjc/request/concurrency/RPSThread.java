package org.alksndrstjc.request.concurrency;

public class RPSThread extends Thread {

    @Override
    public void run() {
        while (!SharedStatsRPS.TERMINATE_RPS.get()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
            }
            SharedStatsRPS.setLatestRPS();
        }
    }

    public void start() {
        this.setDaemon(true);
        super.start();
    }
}
