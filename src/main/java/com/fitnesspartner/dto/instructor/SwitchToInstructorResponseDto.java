package com.fitnesspartner.dto.instructor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SwitchToInstructorResponseDto {

    private String instructorName;

    private String instructorUsername;

    private String addressSido;

    private String addressSigungu;

    private String addressRoadName;

    private String addressDetails;
}
