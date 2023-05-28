package com.fitnesspartner.dto.instructor;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
public class SwitchToInstructorRequestDto {

    @NotBlank(message = "주소(시/도)를 입력해주세요.")
    private String addressSido;

    @NotBlank(message = "주소(시/군/구)를 입력해주세요.")
    private String addressSigungu;

    @NotBlank(message = "주소(도로명)를 입력해주세요.")
    private String addressRoadName;

    private String addressDetails;
}
