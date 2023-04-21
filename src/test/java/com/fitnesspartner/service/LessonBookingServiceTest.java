package com.fitnesspartner.service;

import com.fitnesspartner.constants.Gender;
import com.fitnesspartner.constants.InstructorState;
import com.fitnesspartner.constants.LessonState;
import com.fitnesspartner.constants.UserState;
import com.fitnesspartner.domain.*;
import com.fitnesspartner.dto.lessonBooking.LessonBookingGetAllByUserResponseDto;
import com.fitnesspartner.dto.lessonBooking.LessonBookingRemoveRequestDto;
import com.fitnesspartner.dto.lessonBooking.LessonBookingResponseDto;
import com.fitnesspartner.exception.ClientExceptionCode;
import com.fitnesspartner.exception.RestApiException;
import com.fitnesspartner.repository.*;
import com.fitnesspartner.security.authentication.CustomUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
class LessonBookingServiceTest {

    @Autowired
    LessonBookingRepository lessonBookingRepository;

    @Autowired
    LessonBookingService lessonBookingService;

    @Autowired
    LessonRepository lessonRepository;

    @Autowired
    InstructorRepository instructorRepository;

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    LessonMemberRepository lessonMemberRepository;



    Users studentUsers;

    LessonMember lessonMember;

    Users instructorUsers;
    Instructor instructor;
    Lesson lesson;

    @BeforeEach
    void beforeEach() {
        instructorUsers = Users.builder()
                .username("instructor")
                .name("강사")
                .nickname("김강사")
                .password("12345678")
                .email("nahealth321@Naver.com")
                .phoneNumber("01022223333")
                .gender(Gender.MALE)
                .userState(UserState.Enabled)
                .build();
        usersRepository.save(instructorUsers);

        studentUsers = Users.builder()
                .username("student")
                .name("수강생")
                .nickname("김수강생")
                .password("12345678")
                .email("nahealth123@Naver.com")
                .phoneNumber("01055556666")
                .gender(Gender.MALE)
                .userState(UserState.Enabled)
                .build();
        usersRepository.save(studentUsers);

        lessonMember = LessonMember.builder()
                .users(studentUsers)
                .build();
        lessonMemberRepository.save(lessonMember);

        instructor = Instructor.builder()
                .users(instructorUsers)
                .addressSido("서울")
                .addressSigungu("강남구")
                .addressRoadName("봉은사로")
                .instructorState(InstructorState.Enabled)
                .build();
        instructorRepository.save(instructor);

        LocalDateTime testDateTime = LocalDateTime.now().minusDays(1);
        LocalDateTime endDateTime = testDateTime.plusHours(1);

        lesson = Lesson.builder()
                .lessonName("나강사의 개인PT")
                .lessonDescription("개인으로 진행되는 트레이닝")
                .centerName("나강사 헬스장")
                .centerAddress("서울 강남구 봉은사로 17")
                .maxEnrollment(1)
                .instructor(instructor)
                .startDateTime(testDateTime)
                .endDateTime(endDateTime)
                .lessonState(LessonState.Enabled)
                .build();
        lessonRepository.save(lesson);

    }

    @Nested
    class 성공케이스 {

        @Test
        @DisplayName("레슨 예약")
        void 성공케이스1() {
            // given
            Long lessonId = lesson.getLessonId();
            CustomUserDetails userDetails = CustomUserDetails.builder()
                    .users(studentUsers)
                    .build();

            // when
            String responseMessage = lessonBookingService.lessonBookingCreate(lessonId, userDetails);

            // then
            assertEquals("레슨 예약이 완료되었습니다.", responseMessage);
        }

        @Test
        @DisplayName("레슨 예약 취소(삭제)")
        void 성공케이스2() {
            // given
            Long lessonId = lesson.getLessonId();
            CustomUserDetails userDetails = CustomUserDetails.builder()
                    .users(studentUsers)
                    .build();

            lessonBookingService.lessonBookingCreate(lessonId, userDetails);
            LessonBooking lessonBooking = lessonBookingRepository.findAllByLessonMember(lessonMember).get(0);

            LessonBookingRemoveRequestDto requestDto = new LessonBookingRemoveRequestDto(
                    lesson.getLessonId(),
                    lessonBooking.getLessonBookingId()
            );

            // when
            String responseMessage = lessonBookingService.lessonBookingRemove(requestDto, userDetails);

            // then
            assertEquals("레슨 취소가 완료되었습니다.", responseMessage);
        }

        @Test
        @DisplayName("레슨 예약 정보조회")
        void 성공케이스3() {
            // given
            Long lessonId = lesson.getLessonId();
            CustomUserDetails userDetails = CustomUserDetails.builder()
                    .users(studentUsers)
                    .build();

            lessonBookingService.lessonBookingCreate(lessonId, userDetails);
            LessonBooking lessonBooking = lessonBookingRepository.findAllByLessonMember(lessonMember).get(0);
            Long lessonBookingId = lessonBooking.getLessonBookingId();

            // when
            LessonBookingResponseDto responseDto = lessonBookingService.lessonBookingInfo(lessonBookingId, userDetails);

            // then
            assertEquals(lesson.getLessonId(), responseDto.getLessonId());
            assertEquals(lesson.getLessonName(), responseDto.getLessonName());
            assertEquals(lesson.getStartDateTime(), responseDto.getStartDateTime());
            assertEquals(lesson.getEndDateTime(), responseDto.getEndDateTime());
            assertEquals(studentUsers.getUsername(), responseDto.getLessonBookingUsername());
        }

        @Test
        @DisplayName("유저가 예약한 레슨 정보 전체 조회")
        void 성공케이스4() {
            // given
            Long lessonId = lesson.getLessonId();
            CustomUserDetails userDetails = CustomUserDetails.builder()
                    .users(studentUsers)
                    .build();

            lessonBookingService.lessonBookingCreate(lessonId, userDetails);
            Long LessonMemberId = lessonMember.getLessonMemberId();

            // when
            LessonBookingGetAllByUserResponseDto responseDto = lessonBookingService.lessonBookingGetAll(userDetails, LessonMemberId);
            List<LessonBookingResponseDto> lessonBookingResponseDtoList = responseDto.getLessonBookingList();
            LessonBookingResponseDto lessonBookingResponseDto = lessonBookingResponseDtoList.get(0);

            // then
            assertEquals(lesson.getLessonId(), lessonBookingResponseDto.getLessonId());
            assertEquals(lesson.getLessonName(), lessonBookingResponseDto.getLessonName());
            assertEquals(lesson.getStartDateTime(), lessonBookingResponseDto.getStartDateTime());
            assertEquals(lesson.getEndDateTime(), lessonBookingResponseDto.getEndDateTime());
            assertEquals(instructorUsers.getUsername(), lessonBookingResponseDto.getInstructorUsername());
            assertEquals(instructorUsers.getNickname(), lessonBookingResponseDto.getInstructorNickname());
        }
    }


    @Nested
    class 실패케이스 {

        @Test
        @DisplayName("중복 예약")
        void 실패케이스1() {
            // given
            Long lessonId = lesson.getLessonId();
            CustomUserDetails userDetails = CustomUserDetails.builder()
                    .users(studentUsers)
                    .build();
            lessonBookingService.lessonBookingCreate(lessonId, userDetails);

            // when
            RestApiException exception = assertThrows(RestApiException.class,
                    () -> lessonBookingService.lessonBookingCreate(lessonId, userDetails)
            );

            // then
            assertEquals(ClientExceptionCode.ALREADY_BOOKED_LESSON, exception.getErrorCode());
        }

        @Test
        @DisplayName("취소(삭제)할 레슨 예약이 없음")
        void 실패케이스2() {
            // given
            LessonBookingRemoveRequestDto requestDto = new LessonBookingRemoveRequestDto(
                    lesson.getLessonId(),
                    9999L
            );
            CustomUserDetails userDetails = CustomUserDetails.builder()
                    .users(studentUsers)
                    .build();

            // when
            RestApiException exception = assertThrows(RestApiException.class,
                    () -> lessonBookingService.lessonBookingRemove(requestDto, userDetails)
            );

            // then
            assertEquals(ClientExceptionCode.CANT_FIND_LESSON_BOOKING, exception.getErrorCode());
        }

        @Test
        @DisplayName("없는 레슨 예약 조회")
        void 실패케이스3() {
            // given
            Long fakeLessonBookingId = 9999L;
            CustomUserDetails userDetails = CustomUserDetails.builder()
                    .users(studentUsers)
                    .build();

            // when
//            lessonBookingService.lessonBookingInfo(fakeLessonBookingId, userDetails);
            RestApiException exception = assertThrows(RestApiException.class,
                    () -> lessonBookingService.lessonBookingInfo(fakeLessonBookingId, userDetails)
            );

//            // then
            assertEquals(ClientExceptionCode.CANT_FIND_LESSON_BOOKING, exception.getErrorCode());
        }

        @Test
        @DisplayName("조회할 유저의 레슨 예약들이 없음")
        void 실패케이스4() {
            // given
            CustomUserDetails userDetails = CustomUserDetails.builder()
                    .users(studentUsers)
                    .build();
            Long LessonMemberId = lessonMember.getLessonMemberId();

            // when
            RestApiException exception = assertThrows(RestApiException.class,
                    () -> lessonBookingService.lessonBookingGetAll(userDetails, LessonMemberId)
            );

            // then
            assertEquals(ClientExceptionCode.CANT_FIND_LESSON_BOOKING, exception.getErrorCode());
        }
    }
}