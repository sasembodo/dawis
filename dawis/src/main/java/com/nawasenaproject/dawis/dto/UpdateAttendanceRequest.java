package com.nawasenaproject.dawis.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateAttendanceRequest {

    @Pattern(
            regexp = "^\\d{4}-\\d{2}-\\d{2}$",
            message = "Date must be in format yyyy-MM-dd"
    )
    private String date;

    @Min(value = 0, message = "Special wage cannot be negative")
    private Integer specialWage;

    @NotNull(message = "Mandays is required")
    @DecimalMin(value = "0.5", message = "Mandays must be at least 0.5")
    private Double mandays;

    @Min(value = 0, message = "Bonuses cannot be negative")
    private Integer bonuses;

    @Min(value = 0, message = "Advances cannot be negative")
    private Integer advances;

    @Min(value = 1)
    @Max(value = 2)
    private Integer paidStatus;
}
