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
public class ProjectReportResponse {

    private String id;

    private String code;

    private String name;

    private String type;

    private String location;

    private String coordinates;

    private String status;

    private LocalDate startDate;

    private LocalDate finishDate;

    private int totalUnpaid;

    private List<ProjectReportWorkersResponse> workers;

}
