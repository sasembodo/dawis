package com.nawasenaproject.dawis.enums;

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
    public static ProjectType fromString(String value) {
        if (value == null) return null;

        value = value.trim();

        if (value.contains("-")) {
            value = value.substring(0, 3);
        }

        for (ProjectType type : values()) {
            if (type.code.equalsIgnoreCase(value)) {
                return type;
            }
        }

        throw new IllegalArgumentException("Invalid project type: " + value);
    }

    @Override
    public String toString() {
        return code + "-" + description;
    }
}
