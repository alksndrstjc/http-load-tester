package org.alksndrstjc.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;

public class RequestBuilder {

    private final String url;

    public RequestBuilder(String url) {
        this.url = url;
    }

    public HttpRequest buildRequest() throws URISyntaxException {
        return HttpRequest.newBuilder()
                .GET()
                .uri(new URI(url))
                .build();
    }
}
