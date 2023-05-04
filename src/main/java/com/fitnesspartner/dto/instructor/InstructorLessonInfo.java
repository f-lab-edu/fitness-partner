package com.fitnesspartner.dto.instructor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InstructorLessonInfo {

    private String username;

    private String lessonName;

    private int maxEnrollment;

    private String centerName;

    private String centerAddress;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;
}
