package com.fitnesspartner.dto.instructor;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InstructorInfoResponseDto {

    private String instructorNickname;

    private String addressSido;

    private String addressSigungu;

    private String addressRoadName;

    private String addressDetails;
}
