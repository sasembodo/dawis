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
public class ProjectReportWorkersResponse {

    private String id;

    private String name;

    private String nip;

    private LocalDate recruitDate;

    private String position;

    private int wage;

    private int totalBonus;

    private int totalAdvances;

    private double totalManday;

    private int totalWage;

    private int totalUnpaid;

    private List<ProjectReportWorkersAttendancesResponse> attendances;

}
