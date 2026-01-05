package com.nawasenaproject.dawis.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WorkerReportResponse {

    private String id;

    private String name;

    private String nip;

    private LocalDate recruitDate;

    private String position;

    private Integer wage;

    private Double totalManday;

    private Integer totalBonus;

    private Integer totalAdvance;

    private Integer totalWage;

    private Integer totalUnpaid;

    private List<WorkerReportAttendanceResponse> attendances;
}
