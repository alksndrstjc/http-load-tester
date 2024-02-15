package org.alksndrstjc.commands;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import junit.framework.TestCase;

import static org.junit.Assert.assertThrows;

public class CLIParserTest extends TestCase {

    private JCommander commander;
    private CLIParameters parameters;

    public void setUp() {
        parameters = new CLIParameters();
        commander = JCommander.newBuilder()
                .addObject(parameters)
                .build();
    }

    public void testParsingCorrect() {
        String[] args = {"-u", "http://localhost:8080/endpoint", "-n", "10", "-c", "5"};

        commander.parse(args);

        assertEquals(parameters.url, "http://localhost:8080/endpoint");
        assertEquals(parameters.numberOfRequests, Integer.valueOf(10));
        assertEquals(parameters.numberOfThreads, Integer.valueOf(5));
    }

    public void testParsingBadUrl() {
        String[] args = {"-u", "noturlurl", "-n", "65", "-c", "5"};
        ParameterException exception = assertThrows(ParameterException.class, () -> commander.parse(args));
        assertEquals("String 'noturlurl' is not a valid URL.", exception.getMessage());

    }

    public void testParsingNumberOfCallsLessThan0() {
        String[] args = {"-u", "http://localhost:8080/endpoint", "-n", "-65", "-c", "5"};
        ParameterException exception = assertThrows(ParameterException.class, () -> commander.parse(args));
        assertEquals("Number of calls cannot be a negative value.", exception.getMessage());
    }

    public void testParsingNumberOfThreadsLessThan0() {
        String[] args = {"-u", "http://localhost:8080/endpoint", "-n", "65", "-c", "-50"};
        ParameterException exception = assertThrows(ParameterException.class, () -> commander.parse(args));
        assertEquals("Number of calls cannot be a negative value.", exception.getMessage());
    }


}