package org.alksndrstjc;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import org.alksndrstjc.commands.CLIParameters;
import org.alksndrstjc.commands.CLIParser;
import org.alksndrstjc.model.FinalReportModel;
import org.alksndrstjc.model.ReportModel;
import org.alksndrstjc.request.RequestHandler;
import org.alksndrstjc.request.RequestThreadScheduler;
import org.alksndrstjc.request.concurrency.RPSThread;
import org.alksndrstjc.utils.TextFileReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) {
        System.out.println(Arrays.toString(args));
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

            List<String> urls = new ArrayList<>();
            if (params.fileName != null && !params.fileName.isEmpty()) {
                urls = TextFileReader.parseUrlsFromFile(params.fileName);
            }

            if (params.url != null && !params.url.isEmpty()) {
                urls.add(params.url);
            }

            // start request per second monitor thread
            RPSThread rpsThread = new RPSThread();
            rpsThread.start();

            // start concurrent execution of requests
            ReportModel reportModel = new ReportModel(params.numberOfRequests);

            try (ExecutorService executor = Executors.newFixedThreadPool(params.numberOfThreads + 1)) {
                RequestThreadScheduler handler = new RequestThreadScheduler(executor);
                for (String url : urls) {
                    handler.scheduleRequest(
                            new RequestHandler(url, params.headers, params.method, params.body),
                            params.numberOfRequests,
                            params.numberOfThreads,
                            reportModel
                    );
                }
            }

            // print out report
            for (String url : urls) {
                System.out.println(FinalReportModel.init(url, reportModel));
            }

        } catch (ParameterException | IllegalArgumentException | IOException ex) {
            System.err.println(ex.getMessage());
            jc.usage();
        }
    }

}