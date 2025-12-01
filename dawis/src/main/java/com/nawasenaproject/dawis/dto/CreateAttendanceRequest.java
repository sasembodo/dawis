package com.nawasenaproject.dawis.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateAttendanceRequest {

    private String date;

    private Integer specialWage;

    @NotNull
    private Integer mandays;

    private Integer bonuses;

    private Integer advances;

    private String projectId;

    private String workerId;

}
