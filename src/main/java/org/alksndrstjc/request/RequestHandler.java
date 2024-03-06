package org.alksndrstjc.request;

import lombok.Getter;
import org.alksndrstjc.model.RequestStats;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class RequestHandler {

    @Getter
    private final HttpRequest request;
    private final String body;

    public static class RequiredData {
        public int code;
        public RequestStats requestStats;
        public long endTimeStamp;

        public RequiredData(int code, RequestStats requestStats, long endTimeStamp) {
            this.code = code;
            this.requestStats = requestStats;
            this.endTimeStamp = endTimeStamp;
        }
    }
    public RequestHandler(HttpRequest request, String body) {
        this.request = request;
        this.body = body;
    }

    public RequiredData doRequest() {
        try {
            HttpURLConnection connection = (HttpURLConnection) request.uri().toURL().openConnection();
            // set method
            connection.setRequestMethod(request.method());
            // set headers
            Map<String, List<String>> headerMap = request.headers().map();
            for (Map.Entry<String, List<String>> header : headerMap.entrySet()) {
                connection.setRequestProperty(header.getKey(), String.join(",", header.getValue()));
            }
            // set body
            if (!body.isEmpty()) {
                connection.setDoOutput(true);
                try (OutputStream outputStream = connection.getOutputStream()) {
                    outputStream.write(body.getBytes(StandardCharsets.UTF_8));
                }
            }

            long start = System.currentTimeMillis();
            long timeToFirstByte = 0;
            long timeToLastByte;

            try (InputStream inputStream = connection.getInputStream()) {
                boolean isFirstByte = true;

                while (inputStream.read() != -1) {
                    if (isFirstByte) {
                        isFirstByte = false;
                        timeToFirstByte = System.currentTimeMillis();
                    }
                }
                timeToLastByte = System.currentTimeMillis();
            }
            connection.getResponseCode();
            connection.disconnect();

            long end = System.currentTimeMillis();

            return new RequiredData(
                    connection.getResponseCode(),
                    new RequestStats(
                            (double) (end - start) / 1000,
                            (double) (timeToFirstByte - start) / 1000,
                            (double) (timeToLastByte - start) / 1000
                    ),
                    end
            );

        } catch (IOException e) {
            return null;
        }
    }

}
