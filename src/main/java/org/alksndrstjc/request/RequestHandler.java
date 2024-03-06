package org.alksndrstjc.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.alksndrstjc.model.RequestStats;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Getter
@AllArgsConstructor
public class RequestHandler {

    private final String url;
    private final List<String> headers;
    private final HttpMethodName method;
    private final String body;

    @AllArgsConstructor
    public static class RequiredData {
        public int code;
        public RequestStats requestStats;
        public long endTimeStamp;
    }

    public RequiredData doRequest() {
        try {

            HttpURLConnection connection = (HttpURLConnection) new URI(url).toURL().openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(2500);
            // set method
            connection.setRequestMethod(method != null ? method.getValue() : HttpMethodName.GET.getValue());
            // set headers
            if (headers != null) {
                for (String header : headers) {
                    String[] split = header.split(":");
                    connection.setRequestProperty(split[0].trim(), split[1].trim());
                }
            }
            // set body
            if (body != null && !body.isEmpty()) {
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

        } catch (IOException | URISyntaxException e) {
            return null;
        }
    }

}
