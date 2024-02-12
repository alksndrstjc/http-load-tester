package org.alksndrstjc.utils;

import java.text.DecimalFormat;

public class DecimalFormatter {

    public static double format(double number) {
        DecimalFormat df = new DecimalFormat("#.##");
        return Double.parseDouble(df.format(number));
    }
}
