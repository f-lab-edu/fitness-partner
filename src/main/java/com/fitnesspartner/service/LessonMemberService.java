package com.fitnesspartner.service;

import com.fitnesspartner.domain.LessonMember;
import com.fitnesspartner.domain.UserRoles;
import com.fitnesspartner.domain.Users;
import com.fitnesspartner.dto.lessonMember.LessonMemberResponseDto;
import com.fitnesspartner.exception.ClientExceptionCode;
import com.fitnesspartner.exception.RestApiException;
import com.fitnesspartner.repository.LessonMemberRepository;
import com.fitnesspartner.repository.UserRolesRepository;
import com.fitnesspartner.security.authentication.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.fitnesspartner.security.Roles.LESSON_MEMBER;

@Service
@RequiredArgsConstructor
public class LessonMemberService {

    private final LessonMemberRepository lessonMemberRepository;

    private final UserRolesRepository userRolesRepository;

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

        UserRoles userRole = UserRoles.builder()
                .users(user)
                .roleName(LESSON_MEMBER.getRoleName())
                .build();

        userRolesRepository.save(userRole);

        return lessonMember;
    }

    private LessonMember findLessonMemberById(Long lessonMemberId) {
        return lessonMemberRepository.findById(lessonMemberId)
                .orElseThrow(
                        () -> new RestApiException(ClientExceptionCode.CANT_FIND_LESSON_MEMBER)
                );
    }
}
