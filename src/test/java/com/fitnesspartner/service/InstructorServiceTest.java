package com.fitnesspartner.service;

import com.fitnesspartner.constants.Gender;
import com.fitnesspartner.constants.InstructorState;
import com.fitnesspartner.constants.LessonState;
import com.fitnesspartner.constants.UserState;
import com.fitnesspartner.domain.Instructor;
import com.fitnesspartner.domain.Lesson;
import com.fitnesspartner.domain.Users;
import com.fitnesspartner.dto.instructor.*;
import com.fitnesspartner.dto.lesson.LessonCreateRequestDto;
import com.fitnesspartner.exception.ClientExceptionCode;
import com.fitnesspartner.exception.RestApiException;
import com.fitnesspartner.repository.InstructorRepository;
import com.fitnesspartner.repository.LessonRepository;
import com.fitnesspartner.repository.UsersRepository;
import com.fitnesspartner.security.authentication.CustomUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class InstructorServiceTest {

    @Autowired
    InstructorService instructorService;

    @Autowired
    InstructorRepository instructorRepository;

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    LessonRepository lessonRepository;

    private Users users;

    private CustomUserDetails userDetails;

    @BeforeEach
    void BeforeEach() {
        users = Users.builder()
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
        @DisplayName("강사전환")
        void 강사전환() {
            // given
            SwitchToInstructorRequestDto switchToInstructorRequestDto = new SwitchToInstructorRequestDto(
                    "서울",
                    "성복구",
                    "서울숲길",
                    "17번가"
            );

            // when
            instructorService.switchToInstructor(userDetails, switchToInstructorRequestDto);
            Instructor instructor = instructorRepository.findByUsersAndInstructorState(users, InstructorState.Enabled).get();

            // then
            assertEquals(instructor.getAddressSido(), switchToInstructorRequestDto.getAddressSido());
            assertEquals(instructor.getAddressSigungu(), switchToInstructorRequestDto.getAddressSigungu());
            assertEquals(instructor.getAddressRoadName(), switchToInstructorRequestDto.getAddressRoadName());
            assertEquals(instructor.getAddressDetails(), switchToInstructorRequestDto.getAddressDetails());
        }

        @Test
        @DisplayName("강사정보 조회")
        void 강사정보_조회() {
            // given
            String username = users.getUsername();
            String instructorNickname = users.getNickname();
            instructorService.switchToInstructor(userDetails, new SwitchToInstructorRequestDto(
                    "서울",
                    "성복구",
                    "서울숲길",
                    "17번가"
             ));

            // when
            Instructor instructor = instructorRepository.findByUsersAndInstructorState(users, InstructorState.Enabled).get();
            InstructorInfoResponseDto instructorInfoResponseDto  = instructorService.instructorInfo(username);

            // then
            assertEquals(instructorInfoResponseDto.getAddressSido(), instructor.getAddressSido());
            assertEquals(instructorInfoResponseDto.getAddressSigungu(), instructor.getAddressSigungu());
            assertEquals(instructorInfoResponseDto.getAddressRoadName(), instructor.getAddressRoadName());
            assertEquals(instructorInfoResponseDto.getAddressDetails(), instructor.getAddressDetails());
            assertEquals(instructorInfoResponseDto.getInstructorNickname(), instructorNickname);
        }

        @Test
        @DisplayName("강사 활동지역 주소 업데이트")
        void 강사_활동지역_주소_업데이트() {
            // given
            String username = users.getUsername();
            InstructorAddressUpdateRequestDto instructorAddressUpdateRequestDto = new InstructorAddressUpdateRequestDto(
                    username,
                    "서울",
                    "강남구",
                    "봉은사로",
                    "129-1 751빌딩 B2"
            );

            instructorService.switchToInstructor(userDetails, new SwitchToInstructorRequestDto(
                    instructorAddressUpdateRequestDto.getAddressSido(),
                    instructorAddressUpdateRequestDto.getAddressSigungu(),
                    instructorAddressUpdateRequestDto.getAddressRoadName(),
                    instructorAddressUpdateRequestDto.getAddressDetails()
            ));

            // when
            instructorService.instructorAddressUpdate(instructorAddressUpdateRequestDto);
            Instructor instructor = instructorRepository.findByUsersAndInstructorState(users, InstructorState.Enabled).get();

            // then
            assertEquals(instructorAddressUpdateRequestDto.getAddressSido(), instructor.getAddressSido());
            assertEquals(instructorAddressUpdateRequestDto.getAddressSigungu(), instructor.getAddressSigungu());
            assertEquals(instructorAddressUpdateRequestDto.getAddressRoadName(), instructor.getAddressRoadName());
            assertEquals(instructorAddressUpdateRequestDto.getAddressDetails(), instructor.getAddressDetails());
        }

        @Test
        @DisplayName("강사가 들록한 레슨들 정보 조회")
        void 강사_레슨리스트_조회() {
            // given
            CustomUserDetails userDetails = CustomUserDetails.builder()
                    .users(users)
                    .build();

            Instructor instructor = Instructor.builder()
                    .users(users)
                    .addressSido("서울")
                    .addressSigungu("성복구")
                    .addressRoadName("서울숲길")
                    .addressDetails("17번가")
                    .instructorState(InstructorState.Enabled)
                    .build();

            instructorRepository.save(instructor);

            Lesson lesson = Lesson.builder()
                    .lessonName("김계란의 개인PT")
                    .lessonDescription("개인으로 진행되는 트레이닝")
                    .maxEnrollment(1)
                    .centerName("부곡동 파워짐")
                    .centerAddress("서울 강북구 부곡동 없는길 17")
                    .startDateTime(LocalDateTime.of(2023, 3, 30, 15, 0))
                    .endDateTime(LocalDateTime.of(2023, 3, 30, 20, 0))
                    .lessonState(LessonState.Enabled)
                    .instructor(instructor)
                    .build();

            lessonRepository.save(lesson);

            // when
            InstructorLessonsResponseDto responseDto = instructorService.getInstructorLessons(userDetails);
            InstructorLessonInfo instructorLessonInfo = responseDto.getInstructorLessonInfoList().get(0);

            // then
            assertEquals(userDetails.getUsername(), instructorLessonInfo.getUsername());
            assertEquals(lesson.getLessonName(), instructorLessonInfo.getLessonName());
            assertEquals(lesson.getMaxEnrollment(), instructorLessonInfo.getMaxEnrollment());
            assertEquals(lesson.getCenterName(), instructorLessonInfo.getCenterName());
            assertEquals(lesson.getCenterAddress(), instructorLessonInfo.getCenterAddress());
            assertEquals(lesson.getStartDateTime(), instructorLessonInfo.getStartDateTime());
            assertEquals(lesson.getEndDateTime(), instructorLessonInfo.getEndDateTime());
        }
    }

    @Nested
    class 실패케이스 {

        @Test
        @DisplayName("강사정보 조회 실패 : 유저를 찾을 수 없습니다")
        void 강사정보_조회_실패() {
            // given
            String username = "잘못된유저아이디";

            // when
            RestApiException restApiException = assertThrows(RestApiException.class, () -> {
                instructorService.instructorInfo(username);
            });

            // then
            assertEquals(restApiException.getErrorCode().getMessage(), ClientExceptionCode.CANT_FIND_USER.getMessage());
        }


        @Test
        @DisplayName("강사 활동지역 주소 업데이트 실패 : 유저를 찾을 수 없습니다.")
        void 강사_활동지역_주소_업데이트_실패() {
            // given
            String username = users.getUsername();
            InstructorAddressUpdateRequestDto instructorAddressUpdateRequestDto = new InstructorAddressUpdateRequestDto(
                    "틀린 유저아이디",
                    "서울",
                    "강남구",
                    "봉은사로",
                    "129-1 751빌딩 B2"
            );

            instructorService.switchToInstructor(userDetails,new SwitchToInstructorRequestDto(
                    instructorAddressUpdateRequestDto.getAddressSido(),
                    instructorAddressUpdateRequestDto.getAddressSigungu(),
                    instructorAddressUpdateRequestDto.getAddressRoadName(),
                    instructorAddressUpdateRequestDto.getAddressDetails()
            ));

            // when
            RestApiException restApiException = assertThrows(RestApiException.class, () -> {
                instructorService.instructorAddressUpdate(instructorAddressUpdateRequestDto);
            });

            // then
            assertEquals(restApiException.getErrorCode().getMessage(), ClientExceptionCode.CANT_FIND_USER.getMessage());
        }
    }
}