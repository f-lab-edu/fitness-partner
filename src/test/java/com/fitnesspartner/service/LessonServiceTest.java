package com.fitnesspartner.service;

import com.fitnesspartner.constants.Gender;
import com.fitnesspartner.constants.InstructorState;
import com.fitnesspartner.constants.UserState;
import com.fitnesspartner.domain.Instructor;
import com.fitnesspartner.domain.Lesson;
import com.fitnesspartner.domain.Users;
import com.fitnesspartner.dto.lesson.LessonCreateRequestDto;
import com.fitnesspartner.dto.lesson.LessonDisableRequestDto;
import com.fitnesspartner.dto.lesson.LessonInfoResponseDto;
import com.fitnesspartner.dto.lesson.LessonUpdateRequestDto;
import com.fitnesspartner.exception.ClientExceptionCode;
import com.fitnesspartner.exception.RestApiException;
import com.fitnesspartner.repository.InstructorRepository;
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

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class LessonServiceTest {

    @Autowired
    LessonRepository lessonRepository;

    @Autowired
    LessonService lessonService;

    @Autowired
    InstructorRepository instructorRepository;

    @Autowired
    UsersRepository usersRepository;

    private String username = "nahealth";

    private Users user;

    private Instructor instructor;

    @BeforeEach
    void beforeEach() {
        user = Users.builder()
                .username(username)
                .name("김계란")
                .nickname("나헬린")
                .password("12345678")
                .email("nahealth123@Naver.com")
                .phoneNumber("01088889999")
                .gender(Gender.MALE)
                .userState(UserState.Enabled)
                .build();

        usersRepository.save(user);

        instructor = Instructor.builder()
                .users(user)
                .addressSido("서울")
                .addressSigungu("성북구")
                .addressRoadName("없는길")
                .addressDetails("17")
                .instructorState(InstructorState.Enabled)
                .build();

         instructorRepository.save(instructor);

    }


    @Nested
    class 성공케이스 {

        @Test
        @DisplayName("레슨 생성")
        void 레슨_생성() {
            // given
            LessonCreateRequestDto requestDto = new LessonCreateRequestDto(
                    username,
                    "김계란의 개인PT",
                    "개인으로 진행되는 트레이닝",
                    1,
                    "부곡동 파워짐",
                    "서울 강북구 부곡동 없는길 17",
                    LocalDateTime.of(2023, 3, 30, 15, 0),
                    LocalDateTime.of(2023, 3, 30, 20, 0)
            );

            // when
            String responseMessage = lessonService.lessonCreate(requestDto);

            // then
            assertEquals(responseMessage, "레슨이 생성되었습니다.");

        }

        @Test
        @DisplayName("레슨 비활성화")
        void 레슨_비활성화() {
            // given
            String lessonName = "김계란의 개인PT";
            LessonCreateRequestDto requestDto = new LessonCreateRequestDto(
                    username,
                    lessonName,
                    "개인으로 진행되는 트레이닝",
                    1,
                    "부곡동 파워짐",
                    "서울 강북구 부곡동 없는길 17",
                    LocalDateTime.of(2023, 3, 30, 15, 0),
                    LocalDateTime.of(2023, 3, 30, 20, 0)
            );
            lessonService.lessonCreate(requestDto);
            Lesson lesson = lessonRepository.findByLessonName(lessonName).get();

            LessonDisableRequestDto lessonDisableRequestDto = new LessonDisableRequestDto(
                    lesson.getLessonId(),
                    username
            );


            // when
            String responseMessage = lessonService.lessonDisable(lessonDisableRequestDto);

            // then
            assertEquals(responseMessage, "레슨이 비활성화 되었습니다.");
        }

        @Test
        @DisplayName("레슨 업데이트")
        void 레슨_업데이트() {
            // given
            String lessonName = "김계란의 개인PT";
            LessonCreateRequestDto requestDto = new LessonCreateRequestDto(
                    username,
                    lessonName,
                    "개인으로 진행되는 트레이닝",
                    1,
                    "부곡동 파워짐",
                    "서울 강북구 부곡동 없는길 17",
                    LocalDateTime.of(2023, 3, 30, 15, 0),
                    LocalDateTime.of(2023, 3, 30, 20, 0)
            );
            lessonService.lessonCreate(requestDto);
            Lesson lesson = lessonRepository.findByLessonName(lessonName).get();

            LessonUpdateRequestDto lessonUpdateRequestDto = new LessonUpdateRequestDto(
                    lesson.getLessonId(),
                    "김계란의 그룹PT",
                    "그룹으로 진행되는 트레이닝",
                    "부곡동 계란짐",
                    5,
                    "서울 강북구 부곡동 있는길 17",
                    LocalDateTime.of(2023, 3, 30, 15, 0),
                    LocalDateTime.of(2023, 3, 30, 20, 0)
            );

            // when
            String responseMessage = lessonService.lessonUpdate(lessonUpdateRequestDto);

            // then
            assertEquals(responseMessage, "레슨 수정을 완료했습니다.");
        }

        @Test
        @DisplayName("레슨정보 조회")
        void 레슨_정보_조회() {
            // given
            String lessonName = "김계란의 개인PT";
            LessonCreateRequestDto requestDto = new LessonCreateRequestDto(
                    username,
                    lessonName,
                    "개인으로 진행되는 트레이닝",
                    1,
                    "부곡동 파워짐",
                    "서울 강북구 부곡동 없는길 17",
                    LocalDateTime.of(2023, 3, 30, 15, 0),
                    LocalDateTime.of(2023, 3, 30, 20, 0)
            );
            lessonService.lessonCreate(requestDto);
            Lesson lesson = lessonRepository.findByLessonName(lessonName).get();

            // when
            LessonInfoResponseDto responseDto = lessonService.lessonInfo(lesson.getLessonId());

            // then
            assertEquals(responseDto.getUsername(), username);
            assertEquals(responseDto.getLessonName(), lesson.getLessonName());
            assertEquals(responseDto.getCenterAddress(), lesson.getCenterAddress());
            assertEquals(responseDto.getCenterName(), lesson.getCenterName());
            assertEquals(responseDto.getStartDateTime(), lesson.getStartDateTime());
            assertEquals(responseDto.getEndDateTime(), lesson.getEndDateTime());
            assertEquals(responseDto.getMaxEnrollment(), lesson.getMaxEnrollment());
        }

    }

    @Nested
    class 실패케이스 {

        @Test
        @DisplayName("레슨 등록 - 레슨명 중복")
        void 레슨등록_실패1() {
            // given
            String lessonName = "김계란의 개인PT";
            LessonCreateRequestDto requestDto = new LessonCreateRequestDto(
                    username,
                    lessonName,
                    "개인으로 진행되는 트레이닝",
                    1,
                    "부곡동 파워짐",
                    "서울 강북구 부곡동 없는길 17",
                    LocalDateTime.of(2023, 3, 30, 15, 0),
                    LocalDateTime.of(2023, 3, 30, 20, 0)
            );
            lessonService.lessonCreate(requestDto);

            LessonCreateRequestDto requestDto2 = new LessonCreateRequestDto(
                    username,
                    lessonName,
                    "개인으로 진행되는 트레이닝",
                    1,
                    "부곡동 파워짐",
                    "서울 강북구 부곡동 없는길 17",
                    LocalDateTime.of(2023, 3, 30, 15, 0),
                    LocalDateTime.of(2023, 3, 30, 20, 0)
            );

            // when
            RestApiException exception = assertThrows(RestApiException.class, () -> {
                    lessonService.lessonCreate(requestDto2);
            });

            // then
            assertEquals(exception.getErrorCode(), ClientExceptionCode.LESSON_NAME_ALREADY_EXISTS);
        }

        @Test
        @DisplayName("레슨 등록 - 시작시간 > 종료 시간")
        void 레슨등록_실패2() {
            // given
            LessonCreateRequestDto requestDto = new LessonCreateRequestDto(
                    username,
                    "김계란의 개인PT",
                    "개인으로 진행되는 트레이닝",
                    1,
                    "부곡동 파워짐",
                    "서울 강북구 부곡동 없는길 17",
                    LocalDateTime.of(2023, 3, 30, 20, 0),
                    LocalDateTime.of(2023, 3, 30, 15, 0)
            );

            // when
            RestApiException exception = assertThrows(RestApiException.class, () -> {
                lessonService.lessonCreate(requestDto);
            });

            // then
            assertEquals(exception.getErrorCode(), ClientExceptionCode.START_TIME_GREATER_THAN_END_TIME);
        }

        @Test
        @DisplayName("유저를 찾을 수 없음")
        void 레슨등록_실패3() {
            // given
            LessonCreateRequestDto requestDto = new LessonCreateRequestDto(
                    "없는 유저",
                    "김계란의 개인PT",
                    "개인으로 진행되는 트레이닝",
                    1,
                    "부곡동 파워짐",
                    "서울 강북구 부곡동 없는길 17",
                    LocalDateTime.of(2023, 3, 30, 20, 0),
                    LocalDateTime.of(2023, 3, 30, 15, 0)
            );

            // when
            RestApiException exception = assertThrows(RestApiException.class, () -> {
                lessonService.lessonCreate(requestDto);
            });

            // then
            assertEquals(exception.getErrorCode(), ClientExceptionCode.CANT_FIND_USER);
        }

        @Test
        @DisplayName("강사를 찾을 수 없음")
        void 레슨등록_실패4() {
            // given
            user = Users.builder()
                    .username("naHellChang")
                    .name("김계란2")
                    .nickname("나계란")
                    .password("12345678")
                    .email("nahealth12@Naver.com")
                    .phoneNumber("01088889999")
                    .gender(Gender.MALE)
                    .userState(UserState.Enabled)
                    .build();

            usersRepository.save(user);
            LessonCreateRequestDto requestDto = new LessonCreateRequestDto(
                    "naHellChang",
                    "나헬창의 개인 PT",
                    "개인으로 진행되는 트레이닝",
                    1,
                    "부곡동 파워짐",
                    "서울 강북구 부곡동 없는길 17",
                    LocalDateTime.of(2023, 3, 30, 20, 0),
                    LocalDateTime.of(2023, 3, 30, 15, 0)
            );

            // when
            RestApiException exception = assertThrows(RestApiException.class, () -> {
                lessonService.lessonCreate(requestDto);
            });

            // then
            assertEquals(exception.getErrorCode(), ClientExceptionCode.CANT_FIND_INSTRUCTOR);
        }


        @Test
        @DisplayName("레슨을 찾을 수 없음")
        void 레슨_비활성화_실패1() {
            // given
            LessonDisableRequestDto requestDto = new LessonDisableRequestDto(
                    -1L,
                    username
            );

            // when
            RestApiException exception = assertThrows(RestApiException.class, () -> {
                lessonService.lessonDisable(requestDto);
            });

            // then
            assertEquals(exception.getErrorCode(), ClientExceptionCode.CANT_FIND_LESSON);
        }

        @Test
        @DisplayName("레슨 강사가 아니라 비활성화 불가")
        void 레슨_비활성화_실패2() {
            // given
            String lessonName = "김계란의 개인PT";
            LessonCreateRequestDto requestDto = new LessonCreateRequestDto(
                    username,
                    lessonName,
                    "개인으로 진행되는 트레이닝",
                    1,
                    "부곡동 파워짐",
                    "서울 강북구 부곡동 없는길 17",
                    LocalDateTime.of(2023, 3, 30, 15, 0),
                    LocalDateTime.of(2023, 3, 30, 20, 0)
            );
            lessonService.lessonCreate(requestDto);
            Lesson lesson = lessonRepository.findByLessonName(lessonName).get();

            LessonDisableRequestDto lessonDisableRequestDto = new LessonDisableRequestDto(
                    lesson.getLessonId(),
                    "강사아님"
            );

            // when
            RestApiException exception = assertThrows(RestApiException.class, () -> {
                lessonService.lessonDisable(lessonDisableRequestDto);
            });

            // then
            assertEquals(exception.getErrorCode(), ClientExceptionCode.NOT_LESSON_INSTRUCTOR);
        }

        @Test
        @DisplayName("레슨정보 업데이트-시작시간 > 종료 시간")
        void 레슨정보_업데이트_실패() {
            // given
            LessonUpdateRequestDto requestDto = new LessonUpdateRequestDto(
                    1L,
                    "김계란의 그룹PT",
                    "그룹으로 진행되는 트레이닝",
                    "부곡동 계란짐",
                    5,
                    "서울 강북구 부곡동 있는길 17",
                    LocalDateTime.of(2023, 3, 30, 20, 0),
                    LocalDateTime.of(2023, 3, 30, 15, 0)
            );

            // when
            RestApiException exception = assertThrows(RestApiException.class, () -> {
                lessonService.lessonUpdate(requestDto);
            });

            //then
            assertEquals(exception.getErrorCode(), ClientExceptionCode.START_TIME_GREATER_THAN_END_TIME);
        }

        @Test
        @DisplayName("레슨정보 업데이트-레슨명 중복")
        void 레슨정보_업데이트_실패2() {
            // given
            String lessonName = "김계란의 개인PT";
            LessonCreateRequestDto requestDto = new LessonCreateRequestDto(
                    username,
                    lessonName,
                    "개인으로 진행되는 트레이닝",
                    1,
                    "부곡동 파워짐",
                    "서울 강북구 부곡동 없는길 17",
                    LocalDateTime.of(2023, 3, 30, 15, 0),
                    LocalDateTime.of(2023, 3, 30, 20, 0)
            );
            lessonService.lessonCreate(requestDto);
            Lesson lesson = lessonRepository.findByLessonName(lessonName).get();

            LessonUpdateRequestDto lessonUpdateRequestDto = new LessonUpdateRequestDto(
                    lesson.getLessonId(),
                    "김계란의 그룹PT",
                    "그룹으로 진행되는 트레이닝",
                    "부곡동 계란짐",
                    5,
                    "서울 강북구 부곡동 있는길 17",
                    LocalDateTime.of(2023, 3, 30, 15, 0),
                    LocalDateTime.of(2023, 3, 30, 20, 0)
            );
            lessonService.lessonUpdate(lessonUpdateRequestDto);


            // when
            RestApiException exception = assertThrows(RestApiException.class, () -> {
                lessonService.lessonUpdate(lessonUpdateRequestDto);
            });

            // then
            assertEquals(exception.getErrorCode(), ClientExceptionCode.LESSON_NAME_ALREADY_EXISTS);
        }

        @Test
        @DisplayName("레슨정보 업데이트-레슨이 없음")
        void 레슨정보_업데이트_실패3() {
            // given
            LessonUpdateRequestDto lessonUpdateRequestDto = new LessonUpdateRequestDto(
                    1000L,
                    "김계란의 그룹PT",
                    "그룹으로 진행되는 트레이닝",
                    "부곡동 계란짐",
                    5,
                    "서울 강북구 부곡동 있는길 17",
                    LocalDateTime.of(2023, 3, 30, 15, 0),
                    LocalDateTime.of(2023, 3, 30, 20, 0)
            );

            // when
            RestApiException exception = assertThrows(RestApiException.class, () -> {
                lessonService.lessonUpdate(lessonUpdateRequestDto);
            });

            // then
            assertEquals(exception.getErrorCode(), ClientExceptionCode.CANT_FIND_LESSON);
        }

        @Test
        @DisplayName("없는 레슨 조회")
        void 레슨_정보_조회() {
            // given
            Long lessonId = 1000L;

            // when
            RestApiException exception = assertThrows(RestApiException.class, () -> {
                lessonService.lessonInfo(lessonId);
            });

            // then
            assertEquals(exception.getErrorCode(), ClientExceptionCode.CANT_FIND_LESSON);
        }


    }
}