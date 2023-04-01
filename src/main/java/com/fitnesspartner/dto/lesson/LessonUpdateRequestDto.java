package com.fitnesspartner.dto.lesson;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class LessonUpdateRequestDto {

    @NotNull(message = "lesson id를 입력해주세요.")
    private Long lessonId;

    private String lessonName;

    private String lessonDescription;

    private String centerName;

    @Size(min = 1, max = 30)
    private Integer maxEnrollment;

    private String centerAddress;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;
}
