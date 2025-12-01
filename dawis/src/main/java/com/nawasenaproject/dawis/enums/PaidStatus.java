package com.nawasenaproject.dawis.enums;

import java.util.Arrays;

public enum PaidStatus {

    UNPAID(1, "Unpaid"),
    PAID(2, "Paid");

    private final int code;
    private final String description;

    PaidStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static PaidStatus fromCode(Integer code) {
        return Arrays.stream(values())
                .filter(s -> s.code == code)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid paid status: " + code));
    }
}
