package org.alksndrstjc.request;

import java.net.http.HttpRequest;

public record HttpMethod(String methodName, HttpRequest.BodyPublisher bodyPublisher) {
}
