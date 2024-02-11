package org.alksndrstjc.request.concurrency;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.atomic.AtomicInteger;

public class RequestTask implements Runnable {

    private final HttpClient client;
    private final int numberOfRequests;
    private final HttpRequest request;
    private final AtomicInteger counterGood;
    private final AtomicInteger counterBad;

    public RequestTask(HttpClient client, int numberOfRequests, HttpRequest request, AtomicInteger counterGood, AtomicInteger counterBad) {
        this.client = client;
        this.numberOfRequests = numberOfRequests;
        this.request = request;
        this.counterGood = counterGood;
        this.counterBad = counterBad;
    }

    @Override
    public void run() {
        for (int i = 0; i < numberOfRequests; i++) {
            try {
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() != 500) {
                    counterGood.incrementAndGet();
                } else counterBad.incrementAndGet();
            } catch (IOException | InterruptedException e) {
                counterBad.incrementAndGet();
            }
        }
    }
}
