package com.nawasenaproject.dawis.enums;

import java.util.Arrays;

public enum ProjectStatus {

    WORK_IN_PROGRESS(1, "Work In Progress"),
    FINISH(2, "Finish");

    private final int code;
    private final String description;

    ProjectStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static ProjectStatus fromCode(Integer code) {
        return Arrays.stream(values())
                .filter(s -> s.code == code)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid project status: " + code));
    }
}