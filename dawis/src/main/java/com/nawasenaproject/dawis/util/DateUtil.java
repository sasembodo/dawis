package com.nawasenaproject.dawis.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtil {

    public static String formatRecruitDate(long millis) {
        Locale locale = new Locale("id", "ID");
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMMM yyyy", locale);
        return sdf.format(new Date(millis));
    }
}
