package org.alksndrstjc.request;

import junit.framework.TestCase;
import org.junit.Assert;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.util.Collections;

import static org.junit.Assert.assertThrows;

public class RequestBuilderTest extends TestCase {


    public void testRequestBuilderUrlOk() throws URISyntaxException {
        // given
        String url = "http://localhost:8080/endpoint";
        // when
        RequestBuilder requestBuilder = new RequestBuilder(url, Collections.emptyMap(), new HttpMethod("GET", HttpRequest.BodyPublishers.noBody()));
        // then
        HttpRequest request = requestBuilder.buildRequest();
        Assert.assertEquals(request.uri(), new URI(url));
    }

    public void testRequestBuilderMalformedUrl() {
        // given
        String url = "notaurl";
        // when
        RequestBuilder builder = new RequestBuilder(url, Collections.emptyMap(), new HttpMethod("GET", HttpRequest.BodyPublishers.noBody()));
        // then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, builder::buildRequest);
        assertEquals("URI with undefined scheme", exception.getMessage());
    }
}