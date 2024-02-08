package org.alksndrstjc.commands.validation;

import com.beust.jcommander.IValueValidator;
import com.beust.jcommander.ParameterException;

import java.net.URI;
import java.net.URISyntaxException;

public class URLValidator implements IValueValidator<String> {

    @Override
    public void validate(String name, String value) throws ParameterException {
        if (!isValidUrl(value)) {
            throw new ParameterException(
                    "String " + value + "is not a valid URL."
            );
        }
    }

    private boolean isValidUrl(String value) {
        try {
            new URI(value);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return true;
    }
}
