package com.nawasenaproject.dawis.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectReportRequest {

    private String startDate;

    private String endDate;

    private String workerNip;

    private Integer page;

    private Integer size;

    private String sortBy;

    private String sortDir;

}
