package com.fitnesspartner.dto.lessonBooking;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
public class LessonBookingCreateRequestDto {

    @NotNull(message = "lesssonId를 입력해주세요")
    private Long lessonId;

    @Size(min = 4, max = 15)
    @NotBlank(message = "유저 아이디를 입력해주세요")
    private String username;
}
