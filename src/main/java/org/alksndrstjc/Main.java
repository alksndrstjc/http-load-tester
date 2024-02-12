package org.alksndrstjc;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import org.alksndrstjc.commands.CLIParameters;
import org.alksndrstjc.commands.CLIParser;
import org.alksndrstjc.model.ReportModel;
import org.alksndrstjc.request.RequestBuilder;
import org.alksndrstjc.request.RequestHandler;
import org.alksndrstjc.request.concurrency.ExecutorsServiceFactory;

import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

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

            try (ExecutorService executor = new ExecutorsServiceFactory().createFixedThreadPool(params.numberOfThreads + 1)) {
                AtomicBoolean terminateRPS = new AtomicBoolean(false);
                AtomicInteger requestCalculator = new AtomicInteger();

                //todo: setup rps daemon thread but maybe there is a better way
                Thread rpsDaemon = new Thread(() -> {
                    while (!terminateRPS.get()) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        int count = requestCalculator.getAndSet(0);
                        System.out.println("Requests per second: " + count);
                    }
                });
                rpsDaemon.setDaemon(true);
                rpsDaemon.start();

                RequestHandler handler = new RequestHandler(executor, HttpClient.newBuilder().build());
                handler.handleRequest(
                        new RequestBuilder(params.url).buildRequest(),
                        params.numberOfCalls,
                        params.numberOfThreads,
                        reportModel,
                        requestCalculator,
                        terminateRPS
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