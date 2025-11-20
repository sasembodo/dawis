package com.nawasenaproject.dawis.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class GenerateUtil {

    public static String nipGenerator(String lastNipFromDb, long now) {

        Locale locale = new Locale("id", "ID");
        SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", locale);
        String monthName = monthFormat.format(now);
        String monthCode = monthName.substring(0, 3).toUpperCase();

        SimpleDateFormat yearFormat = new SimpleDateFormat("yy");
        String yearCode = yearFormat.format(now);

        String lastNipYearCode = "";

        if(lastNipFromDb != null){
            lastNipYearCode = lastNipFromDb.substring(4, 6);
        }

        int index = 1;

        if (Objects.equals(lastNipYearCode, yearCode)) {
            String lastIndexStr = lastNipFromDb.substring(lastNipFromDb.length() - 4);
            int lastIndex = Integer.parseInt(lastIndexStr);

            index = lastIndex + 1;
        }

        // --- build final NIP ---
        return monthCode + "-" + yearCode + String.format("%04d", index);
    }
}
