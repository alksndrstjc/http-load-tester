package org.alksndrstjc.commands.validation;

import com.beust.jcommander.IValueValidator;
import com.beust.jcommander.ParameterException;

public class ValidTextFile implements IValueValidator<String> {
    @Override
    public void validate(String name, String value) throws ParameterException {
        if (!value.endsWith(".txt"))
            throw new ParameterException("String " + value + " is not a valid text file name.");
    }
}
