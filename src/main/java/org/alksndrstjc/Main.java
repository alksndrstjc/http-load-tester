package org.alksndrstjc;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import org.alksndrstjc.commands.CLIParameters;
import org.alksndrstjc.commands.CLIParser;
import org.alksndrstjc.model.FinalReportModel;
import org.alksndrstjc.model.ReportModel;
import org.alksndrstjc.request.RequestBuilder;
import org.alksndrstjc.request.RequestHandler;
import org.alksndrstjc.request.concurrency.ExecutorsServiceFactory;
import org.alksndrstjc.request.concurrency.RPSThread;

import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.util.concurrent.ExecutorService;

public class Main {

    public static void main(String[] args) {
        CLIParser parser = CLIParser.getInstance();
        JCommander jc = parser.buildParser();
        try {
            // parse params
            jc.parse(args);
            CLIParameters params = parser.getParameters();

            if (params.help) {
                jc.usage();
                return;
            }

            // start request per second monitor thread
            RPSThread rpsThread = new RPSThread();
            rpsThread.start();

            // start concurrent execution of requests
            ReportModel reportModel = new ReportModel();
            try (ExecutorService executor = new ExecutorsServiceFactory().createFixedThreadPool(params.numberOfThreads + 1)) {
                RequestHandler handler = new RequestHandler(executor, HttpClient.newBuilder().build());
                handler.handleRequest(
                        new RequestBuilder(params.url).buildRequest(),
                        params.numberOfCalls,
                        params.numberOfThreads,
                        reportModel
                );
            }

            // print out report
            System.out.println(FinalReportModel.init(reportModel));

        } catch (ParameterException | URISyntaxException ex) {
            System.err.println(ex.getMessage());
            jc.usage();
        }
    }

}