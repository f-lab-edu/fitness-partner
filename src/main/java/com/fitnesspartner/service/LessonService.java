package com.fitnesspartner.service;

import com.fitnesspartner.constants.InstructorState;
import com.fitnesspartner.constants.LessonState;
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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LessonService {

    private final LessonRepository lessonRepository;
    private final InstructorRepository instructorRepository;
    private final UsersRepository usersRepository;

    public String lessonCreate(LessonCreateRequestDto requestDto) {

        String lessonName = requestDto.getLessonName();
        lessonNameDuplicateCheck(lessonName);

        String username = requestDto.getUsername();

        Users user = findUserByUsername(username);

        Instructor instructor = findInstructorByUser(user);

        LocalDateTime startDateTime = requestDto.getStartDateTime();
        LocalDateTime endDateTime = requestDto.getEndDateTime();

        timeValidator(startDateTime, endDateTime);

        Lesson lesson = Lesson.builder()
                .lessonName(requestDto.getLessonName())
                .lessonDescription(requestDto.getLessonDescription())
                .maxEnrollment(requestDto.getMaxEnrollment())
                .centerName(requestDto.getCenterName())
                .centerAddress(requestDto.getCenterAddress())
                .startDateTime(startDateTime)
                .endDateTime(endDateTime)
                .lessonState(LessonState.Enabled)
                .instructor(instructor)
                .build();
        lessonRepository.save(lesson);

        return "레슨이 생성되었습니다.";
    }

    @Transactional
    public String lessonDisable(LessonDisableRequestDto requestDto) {

        Long lessonId = requestDto.getLessonId();
        Lesson lesson = findLessonByLessonId(lessonId);

        Instructor instructor = lesson.getInstructor();
        Users users = instructor.getUsers();

        String username = requestDto.getUsername();
        if(!users.getUsername().equals(username)) {
            throw new RestApiException(ClientExceptionCode.NOT_LESSON_INSTRUCTOR);
        }

        lesson.lessonDisable();
        return "레슨이 비활성화 되었습니다.";
    }


    @Transactional
    public String lessonUpdate(LessonUpdateRequestDto requestDto) {
        LocalDateTime starDateTime = requestDto.getStartDateTime();
        LocalDateTime endDateTime= requestDto.getEndDateTime();

        timeValidator(starDateTime, endDateTime);

        Lesson lesson = findLessonByLessonId(requestDto.getLessonId());

        String lessonName = requestDto.getLessonName();
        lessonNameDuplicateCheck(lessonName);

        lesson.lessonUpdate(requestDto);

        return "레슨 수정을 완료했습니다.";
    }

    public LessonInfoResponseDto lessonInfo(Long lessonId) {
        Lesson lesson = findLessonByLessonId(lessonId);
        Instructor instructor = lesson.getInstructor();
        Users users = instructor.getUsers();

        return LessonInfoResponseDto.builder()
                .username(users.getUsername())
                .lessonName(lesson.getLessonName())
                .centerAddress(lesson.getCenterAddress())
                .centerName(lesson.getCenterName())
                .startDateTime(lesson.getStartDateTime())
                .endDateTime(lesson.getEndDateTime())
                .maxEnrollment(lesson.getMaxEnrollment())
                .build();
    }

    private Lesson findLessonByLessonId(Long lessonId) {
        return lessonRepository.findById(lessonId)
                .orElseThrow(
                        () -> new RestApiException(ClientExceptionCode.CANT_FIND_LESSON)
                );
    }

    private Instructor findInstructorByUser(Users user) {
        Instructor instructor = instructorRepository.findByUsersAndInstructorState(user, InstructorState.Enabled)
                .orElseThrow(
                        () -> new RestApiException(ClientExceptionCode.CANT_FIND_INSTRUCTOR)
                );
        return instructor;
    }

    private Users findUserByUsername(String username) {
        return usersRepository.findByUsernameAndUserState(username, UserState.Enabled)
                .orElseThrow(
                        () -> new RestApiException(ClientExceptionCode.CANT_FIND_USER)
                );
    }

    private void timeValidator(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        if(!startDateTime.isBefore(endDateTime)) {
            throw new RestApiException(ClientExceptionCode.START_TIME_GREATER_THAN_END_TIME);
        }
    }

    private void lessonNameDuplicateCheck(String lessonName) {
        lessonRepository.findByLessonName(lessonName)
                .ifPresent(
                        a -> { throw new RestApiException(ClientExceptionCode.LESSON_NAME_ALREADY_EXISTS); }
                );
    }
}
