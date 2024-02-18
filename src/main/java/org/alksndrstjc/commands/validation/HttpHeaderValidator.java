package org.alksndrstjc.commands.validation;

import com.beust.jcommander.IValueValidator;
import com.beust.jcommander.ParameterException;

import java.util.List;
import java.util.regex.Pattern;

public class HttpHeaderValidator implements IValueValidator<List<String>> {
    @Override
    public void validate(String name, List<String> headers) throws ParameterException {
        boolean shouldThrowAnException = headers.size() % 2 != 0;

        for (String header : headers) {
            if (!isValidHttpHeader(header)) {
                shouldThrowAnException = true;
                break;
            }
        }

        if (shouldThrowAnException) throw new ParameterException("Not a valid header.");

    }

    public boolean isValidHttpHeader(String header) {
        String pattern = "^[\\w-]+:\\s.*$";
        return Pattern.matches(pattern, header);
    }
}
