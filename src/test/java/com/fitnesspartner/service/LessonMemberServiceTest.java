package com.fitnesspartner.service;

import com.fitnesspartner.constants.Gender;
import com.fitnesspartner.constants.UserState;
import com.fitnesspartner.domain.Users;
import com.fitnesspartner.dto.lessonMember.LessonMemberResponseDto;
import com.fitnesspartner.exception.ClientExceptionCode;
import com.fitnesspartner.exception.RestApiException;
import com.fitnesspartner.repository.LessonMemberRepository;
import com.fitnesspartner.repository.UserRolesRepository;
import com.fitnesspartner.repository.UsersRepository;
import com.fitnesspartner.security.authentication.CustomUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@Transactional
@SpringBootTest
class LessonMemberServiceTest {

    @Autowired
    LessonMemberRepository lessonMemberRepository;

    @Autowired
    LessonMemberService lessonMemberService;

    @Autowired
    UsersRepository usersRepository;

    @MockBean
    UserRolesRepository userRolesRepository;

    CustomUserDetails userDetails;

    @BeforeEach
    void beforeEach() {
        Users users = Users.builder()
                .username("nahealth")
                .name("김계란")
                .nickname("나헬린")
                .password("12345678")
                .email("nahealth123@Naver.com")
                .phoneNumber("01088889999")
                .gender(Gender.MALE)
                .userState(UserState.Enabled)
                .build();
        usersRepository.save(users);

        userDetails = CustomUserDetails.builder()
                .users(users)
                .build();
    }

    @Nested
    class 성공케이스 {

        @Test
        @DisplayName("레슨멤버로 전환")
        void 레슨멤버_전환() {
            // given
            Users user = userDetails.getUsers();

            // when
            LessonMemberResponseDto responseDto = lessonMemberService.switchToLessonMember(userDetails);

            // then
            assertEquals(user.getUsername(), responseDto.getUsername());
            assertEquals(user.getNickname(), responseDto.getNickname());
            assertEquals(user.getPhoneNumber(), responseDto.getPhoneNumber());
            assertEquals(user.getEmail(), responseDto.getEmail());
            assertEquals(user.getGender(), responseDto.getGender());
        }

        @Test
        @DisplayName("레슨멤버 정보 조회")
        void 레슨멤버_정보_조회() {
            // given
            LessonMemberResponseDto lessonMemberResponseDto = lessonMemberService.switchToLessonMember(userDetails);
            Long lessonMemberId = lessonMemberResponseDto.getLessonMemberId();

            // when
            LessonMemberResponseDto responseDto = lessonMemberService.lessonMemberInfo(userDetails, lessonMemberId);

            // then
            assertEquals(lessonMemberResponseDto.getUsername(), responseDto.getUsername());
            assertEquals(lessonMemberResponseDto.getNickname(), responseDto.getNickname());
            assertEquals(lessonMemberResponseDto.getPhoneNumber(), responseDto.getPhoneNumber());
            assertEquals(lessonMemberResponseDto.getEmail(), responseDto.getEmail());
            assertEquals(lessonMemberResponseDto.getGender(), responseDto.getGender());
        }
    }


    @Nested
    class 실패케이스 {

        @Test
        @DisplayName("레슨멤버 정보 조회 실페 : 유저를 찾을 수 없음")
        void 레슨멤버_정보_조회_실패1() {
            // given
            Long lessonMemberId = 9999999L;

            // when
            RestApiException exception = assertThrows(RestApiException.class,
                    () -> {
                        lessonMemberService.lessonMemberInfo(userDetails, lessonMemberId);
                    }
            );

            // then
            assertEquals(ClientExceptionCode.CANT_FIND_LESSON_MEMBER, exception.getErrorCode());
        }
    }
}