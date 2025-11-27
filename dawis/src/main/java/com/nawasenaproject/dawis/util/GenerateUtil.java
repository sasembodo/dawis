package com.nawasenaproject.dawis.util;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Objects;

public class GenerateUtil {

    public static String nipGenerator(String lastNipFromDb) {

        long now = System.currentTimeMillis();
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

    public static String projectCodeGenerator(String typeCode, Integer lastIndex) {

        LocalDate currentDate = LocalDate.now();
        String year = String.format("%02d", currentDate.getYear() % 100);
        String month = String.format("%02d", currentDate.getMonthValue());

        int nextIndex = (lastIndex == null) ? 1 : lastIndex + 1;
        String indexFormatted = String.format("%03d", nextIndex);

        return typeCode + "-" + year + "/" + month + "/" + indexFormatted;
    }

}
