package com.diogo.cookup.utils;

public class NumberFormatUtils {

    public static String formatCompact(long number) {
        if (number < 1000) return String.valueOf(number);

        int exp = (int) (Math.log(number) / Math.log(1000));
        String units = "KMBTPE";
        char unit = units.charAt(exp - 1);

        double value = number / Math.pow(1000, exp);

        if (value >= 100 || value % 1 == 0) {
            return String.format("%.0f%c", value, unit);
        } else {
            return String.format("%.1f%c", value, unit);
        }
    }
}
