package com.nawasenaproject.dawis.util;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Locale;

public class DateUtil {

    public static String formatRecruitDate(long millis) {
        Locale locale = new Locale("id", "ID");
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMMM yyyy", locale);
        return sdf.format(new Date(millis));
    }

    public static LocalDate stringToLocalDate(String dateStr) {
        try{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return LocalDate.parse(dateStr, formatter);
        } catch (DateTimeParseException e) {
            return null;
        }
    }
}
