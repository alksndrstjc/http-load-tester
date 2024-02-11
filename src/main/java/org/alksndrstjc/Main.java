package org.alksndrstjc;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import org.alksndrstjc.commands.CLIParameters;
import org.alksndrstjc.commands.CLIParser;
import org.alksndrstjc.model.ReportModel;
import org.alksndrstjc.request.concurrency.ExecutorsServiceFactory;
import org.alksndrstjc.request.RequestBuilder;
import org.alksndrstjc.request.RequestHandler;

import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.util.concurrent.ExecutorService;

public class Main {

    public static void main(String[] args) {
        CLIParser parser = CLIParser.getInstance();
        JCommander jc = parser.buildParser();
        try {
            jc.parse(args);
            CLIParameters params = parser.getParameters();

            if (params.help) {
                jc.usage();
                return;
            }

            ReportModel reportModel = new ReportModel();

            try (ExecutorService executor = new ExecutorsServiceFactory().createFixedThreadPool(params.numberOfThreads)) {
                RequestHandler handler = new RequestHandler(executor, HttpClient.newBuilder().build());
                handler.handleRequest(
                        new RequestBuilder(params.url).buildRequest(),
                        params.numberOfThreads,
                        params.numberOfCalls / params.numberOfThreads,
                        reportModel
                );
            }

            System.out.println("Successes: " + reportModel.getSuccessCounter());
            System.out.println("Failures: " + reportModel.getFailureCounter());

        } catch (ParameterException | URISyntaxException ex) {
            System.err.println(ex.getMessage());
            jc.usage();
        }
    }

}