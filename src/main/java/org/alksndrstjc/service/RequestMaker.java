package org.alksndrstjc.service;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class RequestMaker {

    private final HttpClient client;

    public RequestMaker(HttpClient client) {
        this.client = client;
    }

    public HttpResponse<String> doRequest(HttpRequest request) throws IOException, InterruptedException {
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
