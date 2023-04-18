package com.fitnesspartner.dto.lesson;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class LessonUpdateRequestDto {

    @NotNull(message = "lesson id를 입력해주세요.")
    private Long lessonId;

    private String lessonName;

    @Size(min = 10, max = 100)
    private String lessonDescription;

    private String centerName;

    @Min(1)
    @Max(10)
    private Integer maxEnrollment;

    private String centerAddress;

    @NotNull(message = "시작 날짜와 시간을 입력해주세요.")
    private LocalDateTime startDateTime;

    @NotNull(message = "종료 날짜와 시간을 입력해주세요.")
    private LocalDateTime endDateTime;
}
