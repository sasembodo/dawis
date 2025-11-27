package com.nawasenaproject.dawis.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateProjectRequest {

    @NotBlank
    @JsonIgnore
    private String id;

    @Size(max = 100)
    private String name;

    @Size(max = 255)
    private String location;

    @Size(max = 255)
    private String coordinates;

    private Integer status;
}
