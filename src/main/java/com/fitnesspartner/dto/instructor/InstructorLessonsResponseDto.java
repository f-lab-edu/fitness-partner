package com.fitnesspartner.dto.instructor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InstructorLessonsResponseDto {

    private List<InstructorLessonInfo> instructorLessonInfoList;
}
