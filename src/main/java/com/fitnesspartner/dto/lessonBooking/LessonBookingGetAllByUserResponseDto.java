package com.fitnesspartner.dto.lessonBooking;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LessonBookingGetAllByUserResponseDto {
    private List<LessonBookingResponseDto> lessonBookingList;
}
