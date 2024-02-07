package org.alksndrstjc;


import org.junit.Test;
import org.mockito.Mockito;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.Assert.assertEquals;

public class MainTest {

    @Test
    public void testYourMethod() throws Exception {
        HttpClient mockedClient = Mockito.mock(HttpClient.class);
        HttpRequest mockedRequest = Mockito.mock(HttpRequest.class);
        HttpResponse<String> mockedResponse = Mockito.mock(HttpResponse.class);

        Mockito.when(mockedClient.send(Mockito.any(HttpRequest.class), Mockito.any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockedResponse);
        Mockito.when(mockedResponse.statusCode()).thenReturn(200);
        Mockito.when(mockedResponse.body()).thenReturn("jes mi ga");

        // Replace the actual HttpClient with the mocked one
        String[] strings = Main.doRequest(mockedClient, mockedRequest);

        assertEquals("200", strings[0]);
        assertEquals("jes mi ga", strings[1]);
        }
}