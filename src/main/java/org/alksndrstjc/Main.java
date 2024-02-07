package org.alksndrstjc;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Main {

    public static void main(String[] args) {
        try (HttpClient client = HttpClient.newBuilder().build()) {
            String arg = args[0];

            HttpRequest request = requestBuilder(arg);

            String[] requestData = doRequest(client, request);

            System.out.println("Response code: " + requestData[0]);
            System.out.println("Body: " + requestData[1]);


        } catch (IndexOutOfBoundsException
                 | URISyntaxException
                 | IOException
                 | InterruptedException e) {
            System.err.println(e.getMessage());
        }
    }

    public static HttpRequest requestBuilder(String arg) throws URISyntaxException {
        return HttpRequest.newBuilder()
                .GET()
                .uri(new URI(arg))
                .build();
    }

    public static String[] doRequest(HttpClient client, HttpRequest request) throws IOException, InterruptedException {
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return new String[] {String.valueOf(response.statusCode()), response.body()};
    }
}