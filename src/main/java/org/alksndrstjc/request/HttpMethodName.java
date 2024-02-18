package org.alksndrstjc.request;

public enum HttpMethodName {
    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    PATCH("PATCH"),
    DELETE("DELETE"),
    OPTIONS("OPTIONS");

    private final String method;

    HttpMethodName(String method) {
        this.method = method;
    }

    public String getValue() {
        return method;
    }
}
