package org.alksndrstjc;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import org.alksndrstjc.commands.CLIParameters;
import org.alksndrstjc.service.RequestBuilder;
import org.alksndrstjc.service.RequestMaker;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Main {

    public static void main(String[] args) {
        CLIParameters cliParameters = new CLIParameters();

        JCommander jc = JCommander.newBuilder()
                .addObject(cliParameters)
                .build();

        try (HttpClient client = HttpClient.newBuilder().build()) {
            jc.parse(args);
            if (cliParameters.help) {
                jc.usage();
                return;
            }

            RequestMaker maker = new RequestMaker(client);
            HttpRequest request = new RequestBuilder(cliParameters.url).buildRequest();
            for (int i = 0; i < cliParameters.numberOfCalls; i++) {
                HttpResponse<String> response = maker.doRequest(request);
                System.out.println("Response code: " + response.statusCode());
                System.out.println("Response body: " + response.body());
            }

        } catch (ParameterException ex) {
            System.err.println(ex.getMessage());
            jc.usage();
        } catch (URISyntaxException | IOException | InterruptedException e) {
            System.err.println(e.getMessage());
        }
    }

}