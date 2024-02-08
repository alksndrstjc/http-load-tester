package org.alksndrstjc.commands;

import com.beust.jcommander.Parameter;
import org.alksndrstjc.commands.validation.URLValidator;

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
            names = {"-n"},
            description = "A number of calls to make.",
            arity = 1,
            validateValueWith = NumberOfCallsValidator.class
    )
    public Integer numberOfCalls;
}
