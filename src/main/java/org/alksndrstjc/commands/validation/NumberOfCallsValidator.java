package org.alksndrstjc.commands.validation;

import com.beust.jcommander.IValueValidator;
import com.beust.jcommander.ParameterException;

public class NumberOfCallsValidator implements IValueValidator<Integer> {
    @Override
    public void validate(String name, Integer value) throws ParameterException {
        if (value < 0) {
            throw new ParameterException("Number of calls cannot be a negative value.");
        }
    }
}
