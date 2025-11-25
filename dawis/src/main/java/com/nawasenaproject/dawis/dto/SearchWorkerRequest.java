package com.nawasenaproject.dawis.dto;

import com.nawasenaproject.dawis.entity.User;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchWorkerRequest {

    String startDate;

    String endDate;

    String position;

    String minWage;

    String maxWage;

    String sortBy;

    String sortDir;

    @NotNull
    int page;

    @NotNull
    int size;
}
