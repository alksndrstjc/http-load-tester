package org.alksndrstjc;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import org.alksndrstjc.commands.CLIParameters;
import org.alksndrstjc.commands.CLIParser;

public class Main {

    public static void main(String[] args) {
        CLIParser parser = CLIParser.getInstance();
        JCommander jc = parser.buildParser();
        try {
            jc.parse(args);
            CLIParameters params = parser.getParameters();

            if (params.help) {
                jc.usage();
            }

        } catch (ParameterException ex) {
            System.err.println(ex.getMessage());
            jc.usage();
        }

    }

}