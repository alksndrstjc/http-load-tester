package org.alksndrstjc.utils;

import org.alksndrstjc.commands.validation.URLValidator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TextFileReader {

    public static List<String> parseUrlsFromFile(String filename) throws IllegalArgumentException, IOException {
        List<String> urls = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!URLValidator.isValidUrl(line))
                    throw new IllegalArgumentException(line + " is not a valid url.");
                urls.add(line);
            }
        }
        return urls;
    }
}
