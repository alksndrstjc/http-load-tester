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

            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(new URI(arg))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Response code: " + response.statusCode());
            System.out.println("Body: " + response.body());


        } catch (IndexOutOfBoundsException
                 | URISyntaxException
                 | IOException
                 | InterruptedException e) {
            System.err.println(e.getMessage());
        }
    }
}