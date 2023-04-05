package com.fitnesspartner.dto.lessonBooking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LessonBookingResponseDto {

    private Long lessonId;

    private String lessonName;

    private String instructorUsername;

    private String instructorNickname;

    private String lessonBookingUsername;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;
}
