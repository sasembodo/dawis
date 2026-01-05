package com.nawasenaproject.dawis.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WorkerReportProjectResponse {

    private String id;

    private String code;

    private String name;

    private String type;

    private String location;

}
