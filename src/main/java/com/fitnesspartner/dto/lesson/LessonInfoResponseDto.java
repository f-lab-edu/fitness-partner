package com.fitnesspartner.dto.lesson;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class LessonInfoResponseDto {

    private String username;

    private String lessonName;

    private int maxEnrollment;

    private String centerName;

    private String centerAddress;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;
}
