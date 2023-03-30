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
            System.out.println(responseMessage);
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
        @DisplayName("레슨 정보 조회")
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

    }
}