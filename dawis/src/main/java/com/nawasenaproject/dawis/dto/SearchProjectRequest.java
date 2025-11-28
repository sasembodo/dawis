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
public class SearchProjectRequest {

    private String code;

    private String name;

    private String type;

    private String location;

    private String status;

    private String beginStartDate;

    private String endStartDate;

    private String beginFinishDate;

    private String endFinishDate;

    private String sortBy;

    private String sortDir;

    @NotNull
    private Integer page;

    @NotNull
    private Integer size;
}
