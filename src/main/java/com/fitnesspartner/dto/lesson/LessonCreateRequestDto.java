package com.fitnesspartner.dto.lesson;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LessonCreateRequestDto {

    @NotBlank(message = "강사 아이디를 입력해주세요.")
    private String username;

    @NotBlank(message = "레슨명을 입력해주세요.")
    private String lessonName;

    @Size(min = 10, max = 100)
    @NotBlank(message = "레슨 소개글을 입력해주세요.")
    private String lessonDescription;

    @Min(1)
    @Max(10)
    @NotNull(message = "레슨 정원을 입력해주세요.")
    private int maxEnrollment;

    @NotBlank(message = "센터 이름을 입력해주세요.")
    private String centerName;

    @NotBlank(message = "센터 주소를 입력해주세요.")
    private String centerAddress;

    @NotNull(message = "시작 날짜와 시간을 입력해주세요.")
    private LocalDateTime startDateTime;

    @NotNull(message = "종료 날짜와 시간을 입력해주세요.")
    private LocalDateTime endDateTime;
}
