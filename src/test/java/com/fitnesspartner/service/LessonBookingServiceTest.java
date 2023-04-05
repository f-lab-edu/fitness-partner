package com.fitnesspartner.service;

import com.fitnesspartner.constants.Gender;
import com.fitnesspartner.constants.InstructorState;
import com.fitnesspartner.constants.LessonState;
import com.fitnesspartner.constants.UserState;
import com.fitnesspartner.domain.Instructor;
import com.fitnesspartner.domain.Lesson;
import com.fitnesspartner.domain.LessonBooking;
import com.fitnesspartner.domain.Users;
import com.fitnesspartner.dto.lessonBooking.LessonBookingCreateRequestDto;
import com.fitnesspartner.dto.lessonBooking.LessonBookingGetAllByUserResponseDto;
import com.fitnesspartner.dto.lessonBooking.LessonBookingRemoveRequestDto;
import com.fitnesspartner.dto.lessonBooking.LessonBookingResponseDto;
import com.fitnesspartner.repository.InstructorRepository;
import com.fitnesspartner.repository.LessonBookingRepository;
import com.fitnesspartner.repository.LessonRepository;
import com.fitnesspartner.repository.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

    Users studentUsers;
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
            LessonBookingCreateRequestDto requestDto = new LessonBookingCreateRequestDto(
                    lesson.getLessonId(),
                    studentUsers.getUsername()
            );

            // when
            String responseMessage = lessonBookingService.lessonBookingCreate(requestDto);

            // then
            assertEquals("레슨 예약이 완료되었습니다.", responseMessage);
        }

        @Test
        @DisplayName("레슨 예약 취소(삭제)")
        void 성공케이스2() {
            // given
            LessonBookingCreateRequestDto lessonBookingCreateRequestDto = new LessonBookingCreateRequestDto(
                    lesson.getLessonId(),
                    studentUsers.getUsername()
            );
            lessonBookingService.lessonBookingCreate(lessonBookingCreateRequestDto);
            LessonBooking lessonBooking = lessonBookingRepository.findByUsers(studentUsers).get(0);

            LessonBookingRemoveRequestDto requestDto = new LessonBookingRemoveRequestDto(
                    lesson.getLessonId(),
                    lessonBooking.getLessonBookingId(),
                    studentUsers.getUsername()
            );

            // when
            String responseMessage = lessonBookingService.lessonBookingRemove(requestDto);

            // then
            assertEquals("레슨 취소가 완료되었습니다.", responseMessage);
        }

        @Test
        @DisplayName("레슨 예약 정보조회")
        void 성공케이스3() {
            // given
            LessonBookingCreateRequestDto lessonBookingCreateRequestDto = new LessonBookingCreateRequestDto(
                    lesson.getLessonId(),
                    studentUsers.getUsername()
            );
            lessonBookingService.lessonBookingCreate(lessonBookingCreateRequestDto);
            LessonBooking lessonBooking = lessonBookingRepository.findByUsers(studentUsers).get(0);


            // when
            LessonBookingResponseDto responseDto = lessonBookingService.lessonBookingInfo(lessonBooking.getLessonBookingId());

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
            LessonBookingCreateRequestDto lessonBookingCreateRequestDto = new LessonBookingCreateRequestDto(
                    lesson.getLessonId(),
                    studentUsers.getUsername()
            );
            lessonBookingService.lessonBookingCreate(lessonBookingCreateRequestDto);

            // when
            LessonBookingGetAllByUserResponseDto responseDto = lessonBookingService.lessonBookingGetAllByUser(studentUsers.getUsername());
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

    }


}