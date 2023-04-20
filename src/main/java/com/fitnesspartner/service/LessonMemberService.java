package com.fitnesspartner.service;

import com.fitnesspartner.domain.LessonMember;
import com.fitnesspartner.domain.Users;
import com.fitnesspartner.dto.lessonMember.LessonMemberResponseDto;
import com.fitnesspartner.exception.ClientExceptionCode;
import com.fitnesspartner.exception.RestApiException;
import com.fitnesspartner.repository.LessonMemberRepository;
import com.fitnesspartner.security.authentication.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LessonMemberService {

    private final LessonMemberRepository lessonMemberRepository;

    public LessonMemberResponseDto switchToLessonMember(CustomUserDetails userDetails) {

        Users user = userDetails.getUsers();

        LessonMember lessonMember = lessonMemberRepository.findByUsers(user)
                .orElseGet(
                        () -> saveLessonMember(user)
                );

        return LessonMemberResponseDto.builder()
                .lessonMemberId(lessonMember.getLessonMemberId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .gender(user.getGender())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }

    public LessonMemberResponseDto lessonMemberInfo(CustomUserDetails userDetails, Long lessonMemberId) {

        LessonMember lessonMember = findLessonMemberById(lessonMemberId);
        Users user = userDetails.getUsers();

        return LessonMemberResponseDto.builder()
                .lessonMemberId(lessonMember.getLessonMemberId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .gender(user.getGender())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }


    private LessonMember saveLessonMember(Users user) {
        LessonMember lessonMember = LessonMember.builder()
                .users(user)
                .build();

        lessonMemberRepository.save(lessonMember);

        return lessonMember;
    }

    private LessonMember findLessonMemberById(Long lessonMemberId) {
        return lessonMemberRepository.findById(lessonMemberId)
                .orElseThrow(
                        () -> new RestApiException(ClientExceptionCode.CANT_FIND_LESSON_MEMBER)
                );
    }
}
