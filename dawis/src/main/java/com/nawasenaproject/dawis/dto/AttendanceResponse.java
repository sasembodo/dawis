package com.nawasenaproject.dawis.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AttendanceResponse {

    private String id;

    private LocalDate date;

    private Integer specialWage;

    private Double mandays;

    private Integer bonuses;

    private Integer advances;

    private String paidStatus;

    private ProjectResponse project;

    private WorkerResponse worker;
}
