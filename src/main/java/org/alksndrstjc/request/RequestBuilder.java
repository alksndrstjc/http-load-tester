package org.alksndrstjc.request;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.util.Arrays;
import java.util.Map;

public class RequestBuilder {


    private final String url;
    private final Map<String, String> headers;
    private final HttpMethod method;

    public RequestBuilder(String url, Map<String, String> headers, HttpMethod method) {
        this.url = url;
        this.headers = headers;
        this.method = method;
    }

    public HttpRequest buildRequest() throws URISyntaxException {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(new URI(url))
                .method(method.methodName(), method.bodyPublisher());

        if (headers != null && !headers.isEmpty())
            builder.headers(headers.entrySet().stream()
                    .flatMap(entry -> Arrays.stream(new String[]{entry.getKey(), entry.getValue()}))
                    .toArray(String[]::new));

        return builder.build();
    }
}
