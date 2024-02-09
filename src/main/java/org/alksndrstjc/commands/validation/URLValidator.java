package org.alksndrstjc.commands.validation;

import com.beust.jcommander.IValueValidator;
import com.beust.jcommander.ParameterException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;

public class URLValidator implements IValueValidator<String> {

    @Override
    public void validate(String name, String value) throws ParameterException {
        if (!isValidUrl(value)) {
            throw new ParameterException(
                    "String '" + value + "' is not a valid URL."
            );
        }
    }

    private boolean isValidUrl(String value) {
        try {
            URI uri = new URI(value);
            String scheme = uri.getScheme();
            if (scheme == null) return false;
            scheme = scheme.toLowerCase(Locale.US);
            if (!(scheme.equals("https") || scheme.equals("http")) || uri.getHost() == null) return false;

        } catch (URISyntaxException e) {
            return false;
        }
        return true;
    }
}
