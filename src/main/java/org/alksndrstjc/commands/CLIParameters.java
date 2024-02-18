package org.alksndrstjc.commands;

import com.beust.jcommander.Parameter;
import org.alksndrstjc.commands.validation.HttpHeaderValidator;
import org.alksndrstjc.commands.validation.NumberOfCallsValidator;
import org.alksndrstjc.commands.validation.URLValidator;
import org.alksndrstjc.commands.validation.ValidTextFile;
import org.alksndrstjc.request.HttpMethodName;

import java.util.List;

public class CLIParameters {

    @Parameter(names = "--help", help = true)
    public boolean help;

    @Parameter(
            names = {"-u"},
            description = "A valid URL.",
            arity = 1,
            validateValueWith = URLValidator.class
    )
    public String url;

    @Parameter(
            names = {"-m"},
            description = "An HTTP method."
    )
    public HttpMethodName method;

    @Parameter(
            names = {"-H"},
            description = "An HTTP header.",
            variableArity = true,
            validateValueWith = HttpHeaderValidator.class

    )
    public List<String> headers;

    @Parameter(
            names = {"-d"},
            description = "An HTTP Request Body.",
            arity = 1
    )
    public String body;

    @Parameter(
            names = {"-n"},
            description = "A number of calls to make.",
            arity = 1,
            validateValueWith = NumberOfCallsValidator.class
    )
    public Integer numberOfRequests;

    @Parameter(
            names = {"-c",},
            description = "A number of threads to use.",
            arity = 1,
            validateValueWith = NumberOfCallsValidator.class
    )
    public Integer numberOfThreads;

    @Parameter(
            names = {"-f"},
            description = "A file containing urls.",
            arity = 1,
            validateValueWith = ValidTextFile.class
    )
    public String fileName;

}
