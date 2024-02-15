package org.alksndrstjc.commands;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import lombok.Getter;

@Getter
public class CLIParser {

    private final CLIParameters parameters = new CLIParameters();

    private static CLIParser instance;

    public static CLIParser getInstance() {
        if (instance == null) instance = new CLIParser();
        return instance;
    }

    private CLIParser() {
    }

    public JCommander buildParser() throws ParameterException {
        return JCommander.newBuilder()
                .addObject(parameters)
                .build();
    }
}
