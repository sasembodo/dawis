package com.nawasenaproject.dawis.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WorkerResponse {

    private String id;

    private String name;

    private String nip;

    private String recruitDate;

    private String position;

    private int wage;
}
