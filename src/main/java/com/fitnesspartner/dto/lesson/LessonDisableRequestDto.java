package com.fitnesspartner.dto.lesson;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LessonDisableRequestDto {

    private Long lessonId;

    private String username;
}
