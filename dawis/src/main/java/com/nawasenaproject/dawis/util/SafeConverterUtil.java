package com.nawasenaproject.dawis.util;

public class SafeConverterUtil {

    public static Integer toIntegerSafe(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

}
