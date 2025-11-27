package com.nawasenaproject.dawis.enums;

import java.util.Arrays;

public enum ProjectType {

    BGN("BGN", "Konstruksi"),
    REV("REV", "Perbaikan"),
    REB("REB", "Perawatan"),
    FAB("FAB", "Fabrikasi"),
    SUP("SUP", "Pengadaan");

    private final String code;
    private final String description;

    ProjectType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public String getFull() {
        return code + "-" + description;
    }

    public static ProjectType fromCode(String input) {
        if (input == null) return null;

        String codeOnly = input.contains("-") ? input.substring(0, 3) : input;

        return Arrays.stream(values())
                .filter(t -> t.code.equalsIgnoreCase(codeOnly))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid project type: " + input));
    }
}
