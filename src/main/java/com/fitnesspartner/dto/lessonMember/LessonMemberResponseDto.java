package com.fitnesspartner.dto.lessonMember;

import com.fitnesspartner.constants.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LessonMemberResponseDto {

    private Long lessonMemberId;

    private String username;

    private String nickname;

    private String email;

    private Gender gender;

    private String phoneNumber;

}
