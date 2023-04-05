package com.fitnesspartner.service;

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
import com.fitnesspartner.exception.ClientExceptionCode;
import com.fitnesspartner.exception.RestApiException;
import com.fitnesspartner.repository.InstructorRepository;
import com.fitnesspartner.repository.LessonBookingRepository;
import com.fitnesspartner.repository.LessonRepository;
import com.fitnesspartner.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LessonBookingService {

    private final LessonBookingRepository lessonBookingRepository;

    private final LessonRepository lessonRepository;

    private final UsersRepository usersRepository;

    private final InstructorRepository instructorRepository;

    public String lessonBookingCreate(LessonBookingCreateRequestDto requestDto) {
        Long lessonId = requestDto.getLessonId();
        String username = requestDto.getUsername();

        Users user = findUserByUsername(username);

        instructorCantBookLessonCheck(user);

        Lesson lesson = findEnabledLessonByLessonId(lessonId);

        lessonBookingDuplicateCheck(user, lesson);

        LessonBooking lessonBooking = LessonBooking.builder()
                .users(user)
                .lesson(lesson)
                .createdAt(LocalDateTime.now())
                .build();
        lessonBookingRepository.save(lessonBooking);
        return "레슨 예약이 완료되었습니다.";
    }

    public String lessonBookingRemove(LessonBookingRemoveRequestDto requestDto) {
        Long lessonId = requestDto.getLessonId();
        String username = requestDto.getUsername();
        Long lessonBookingId = requestDto.getLessonBookingId();

        userExistsCheckByUsername(username);

        Lesson lesson = findEnabledLessonByLessonId(lessonId);

        cantCancelOnDayLesson(lesson.getStartDateTime());

        LessonBooking lessonBooking = findLessonBookingByLessonBookingId(lessonBookingId);
        lessonBookingRepository.delete(lessonBooking);
        return "레슨 취소가 완료되었습니다.";
    }

    public LessonBookingResponseDto lessonBookingInfo(Long lessonBookingId) {
        LessonBooking lessonBooking = findLessonBookingByLessonBookingId(lessonBookingId);

        Lesson lesson = lessonBooking.getLesson();
        Users user = lessonBooking.getUsers();
        Instructor instructor = lesson.getInstructor();

        Users instructorInfo = instructor.getUsers();

        return LessonBookingResponseDto.builder()
                .lessonId(lesson.getLessonId())
                .lessonName(lesson.getLessonName())
                .instructorUsername(instructorInfo.getUsername())
                .instructorNickname(instructorInfo.getNickname())
                .lessonBookingUsername(user.getUsername())
                .startDateTime(lesson.getStartDateTime())
                .endDateTime(lesson.getEndDateTime())
                .build();
    }

    public LessonBookingGetAllByUserResponseDto lessonBookingGetAllByUser(String username) {
        Users user = findUserByUsername(username);

        List<LessonBooking> lessonBookingList = lessonBookingRepository.findByUsers(user);

        List<LessonBookingResponseDto> lessonBookingResponseDtoList = new ArrayList<>();

        for(LessonBooking lessonBooking : lessonBookingList) {
            Lesson lesson = lessonBooking.getLesson();

            Instructor instructor = lesson.getInstructor();

            Users instructorInfo = instructor.getUsers();

            lessonBookingResponseDtoList.add(
                    LessonBookingResponseDto.builder()
                            .lessonId(lesson.getLessonId())
                            .lessonName(lesson.getLessonName())
                            .instructorUsername(instructorInfo.getUsername())
                            .instructorNickname(instructorInfo.getNickname())
                            .lessonBookingUsername(user.getUsername())
                            .startDateTime(lesson.getStartDateTime())
                            .endDateTime(lesson.getEndDateTime())
                            .build()
            );
        }

        return new LessonBookingGetAllByUserResponseDto(lessonBookingResponseDtoList);
    }


    private void cantCancelOnDayLesson(LocalDateTime startDateTime) {
        LocalDate startDate = startDateTime.toLocalDate();
        LocalDate localDate = LocalDate.now();
        if(startDate.isEqual(localDate)) {
            throw new RestApiException(ClientExceptionCode.CANT_CANCEL_ON_DAY_LESSON);
        }
    }

    private LessonBooking findLessonBookingByLessonBookingId(Long lessonBookingId) {
        return lessonBookingRepository.findById(lessonBookingId)
                .orElseThrow(
                        () -> new RestApiException(ClientExceptionCode.CANT_FIND_LESSON_BOOKING)
                );
    }

    private void userExistsCheckByUsername(String username) {
        usersRepository.existsByUsername(username);
    }

    private Users findUserByUsername(String username) {
        return usersRepository.findByUsernameAndUserState(username, UserState.Enabled)
                .orElseThrow(
                        () -> new RestApiException(ClientExceptionCode.CANT_FIND_USER)
                );
    }

    private Lesson findEnabledLessonByLessonId(Long lessonId) {
        return lessonRepository.findByLessonIdAndLessonState(lessonId, LessonState.Enabled)
                .orElseThrow(
                        () -> new RestApiException(ClientExceptionCode.CANT_FIND_LESSON)
                );
    }

    private void lessonBookingDuplicateCheck(Users user, Lesson lesson) {
        if(lessonBookingRepository.existsByUsersAndLesson(user, lesson)) {
            throw new RestApiException(ClientExceptionCode.ALREADY_BOOKED_LESSON);
        }
    }

    private void instructorCantBookLessonCheck(Users user) {
        if(instructorRepository.existsByUsers(user)) {
            throw new RestApiException(ClientExceptionCode.INSTRUCTOR_CANT_BOOK_LESSON);
        }
    }

}
