package com.fitnesspartner.dto.lessonBooking;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class LessonBookingRemoveRequestDto {

    @NotNull(message = "lessonId를 입력해주세요")
    private Long lessonId;

    @NotNull(message = "lessonBookingId를 입력해주세요")
    private Long lessonBookingId;
}
