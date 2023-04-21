package com.fitnesspartner.service;

import com.fitnesspartner.constants.LessonState;
import com.fitnesspartner.domain.*;
import com.fitnesspartner.dto.lessonBooking.LessonBookingGetAllByUserResponseDto;
import com.fitnesspartner.dto.lessonBooking.LessonBookingRemoveRequestDto;
import com.fitnesspartner.dto.lessonBooking.LessonBookingResponseDto;
import com.fitnesspartner.exception.ClientExceptionCode;
import com.fitnesspartner.exception.RestApiException;
import com.fitnesspartner.repository.LessonBookingRepository;
import com.fitnesspartner.repository.LessonMemberRepository;
import com.fitnesspartner.repository.LessonRepository;
import com.fitnesspartner.security.authentication.CustomUserDetails;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LessonBookingService {

    private final LessonBookingRepository lessonBookingRepository;

    private final LessonRepository lessonRepository;

    private final LessonMemberRepository lessonMemberRepository;

    private final JPAQueryFactory jpaQueryFactory;

    @Transactional
    public String lessonBookingCreate(Long lessonId, CustomUserDetails userDetails) {

        Users user = userDetails.getUsers();

        Lesson lesson = findEnabledLessonByLessonId(lessonId);

        Instructor instructor = lesson.getInstructor();
        Users lessonInstructor = instructor.getUsers();

        lessonInstructorCantBookLesson(user, lessonInstructor);

        LessonMember lessonMember = findLessonMemberByUser(user);

        lessonBookingDuplicateCheck(lessonMember, lesson);

        LessonBooking lessonBooking = LessonBooking.builder()
                .lessonMember(lessonMember)
                .lesson(lesson)
                .createdAt(LocalDateTime.now())
                .build();
        lessonBookingRepository.save(lessonBooking);
        return "레슨 예약이 완료되었습니다.";
    }

    private LessonMember findLessonMemberByUser(Users user) {
        return lessonMemberRepository.findByUsers(user)
                .orElseThrow(
                        () -> new RestApiException(ClientExceptionCode.CANT_FIND_LESSON_MEMBER)
                );
    }

    @Transactional
    public String lessonBookingRemove(LessonBookingRemoveRequestDto requestDto, CustomUserDetails userDetails) {
        Long lessonId = requestDto.getLessonId();
        Long lessonBookingId = requestDto.getLessonBookingId();
        Users authUser = userDetails.getUsers();

        Lesson lesson = findEnabledLessonByLessonId(lessonId);

        cantCancelOnDayLesson(lesson.getStartDateTime());

        LessonBooking lessonBooking = findLessonBookingByLessonBookingId(lessonBookingId);
        LessonMember lessonMember = lessonBooking.getLessonMember();
        Users lessonMemberUser = lessonMember.getUsers();

        authUserCheckEqualLessonMemberUser(authUser, lessonMemberUser);

        lessonBookingRepository.delete(lessonBooking);
        return "레슨 취소가 완료되었습니다.";
    }
    
    public LessonBookingResponseDto lessonBookingInfo(Long lessonBookingId, CustomUserDetails userDetails) {
        existsLessonBooking(lessonBookingId);

        QLessonBooking qLessonBooking = QLessonBooking.lessonBooking;
        QLesson qLesson = QLesson.lesson;
        QInstructor qInstructor = QInstructor.instructor;
        QUsers qUsers = QUsers.users;

        LessonBooking lessonBooking = jpaQueryFactory.selectFrom(qLessonBooking)
                .innerJoin(qLessonBooking.lesson, qLesson).fetchJoin()
                .innerJoin(qLesson.instructor, qInstructor).fetchJoin()
                .innerJoin(qInstructor.users, qUsers).fetchJoin()
                .where(qLessonBooking.lessonBookingId.eq(lessonBookingId))
                .fetchOne();

        Lesson lesson = getLessonByLessonBookingIfExists(lessonBooking);
        Users lessonMemberUser = userDetails.getUsers();

        Instructor instructor = lesson.getInstructor();

        Users instructorInfo = instructor.getUsers();

        return LessonBookingResponseDto.builder()
                .lessonId(lesson.getLessonId())
                .lessonName(lesson.getLessonName())
                .instructorUsername(instructorInfo.getUsername())
                .instructorNickname(instructorInfo.getNickname())
                .lessonBookingUsername(lessonMemberUser.getUsername())
                .startDateTime(lesson.getStartDateTime())
                .endDateTime(lesson.getEndDateTime())
                .build();
    }

    public LessonBookingGetAllByUserResponseDto lessonBookingGetAll(CustomUserDetails userDetails,
                                                                                  Long lessonMemberId) {
        String username = userDetails.getUsername();

        QLessonBooking qLessonBooking = QLessonBooking.lessonBooking;
        QLessonMember qLessonMember = QLessonMember.lessonMember;
        QLesson qLesson = QLesson.lesson;
        QInstructor qInstructor = QInstructor.instructor;
        QUsers qUsers = QUsers.users;

        List<LessonBooking> lessonBookingList = jpaQueryFactory.selectFrom(qLessonBooking)
                .innerJoin(qLessonBooking.lessonMember, qLessonMember).fetchJoin()
                .innerJoin(qLessonBooking.lesson, qLesson).fetchJoin()
                .innerJoin(qLesson.instructor, qInstructor).fetchJoin()
                .innerJoin(qInstructor.users, qUsers).fetchJoin()
                .where(qLessonBooking.lessonMember.lessonMemberId.eq(lessonMemberId))
                .fetch();

        findLessonBookingByLessonMember(lessonBookingList);

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
                            .lessonBookingUsername(username)
                            .startDateTime(lesson.getStartDateTime())
                            .endDateTime(lesson.getEndDateTime())
                            .build()
            );
        }

        return new LessonBookingGetAllByUserResponseDto(lessonBookingResponseDtoList);
    }

    private static Lesson getLessonByLessonBookingIfExists(LessonBooking lessonBooking) {
        Lesson lesson = lessonBooking.getLesson();
        if(lesson == null) {
            throw new RestApiException(ClientExceptionCode.CANT_FIND_LESSON);
        }
        return lesson;
    }

    private void existsLessonBooking(Long lessonBookingId) {
        if(!lessonBookingRepository.existsById(lessonBookingId)) {
            throw new RestApiException(ClientExceptionCode.CANT_FIND_LESSON_BOOKING);
        }
    }

    private void authUserCheckEqualLessonMemberUser(Users authUser, Users lessonMemberUser) {
        if(!authUser.equals(lessonMemberUser)) {
            throw new RestApiException(ClientExceptionCode.NOT_LESSON_BOOKING_MEMBER);
        }
    }

    private void lessonInstructorCantBookLesson(Users authUser, Users instructorUser) {
        if(instructorUser.getUsersId().equals(authUser.getUsersId())) {
            throw new RestApiException(ClientExceptionCode.LESSON_INSTRUCTOR_CANT_BOOK);
        }
    }

    private void findLessonBookingByLessonMember(List<LessonBooking> lessonBookingList) {
        if(lessonBookingList.size() == 0) {
            throw new RestApiException(ClientExceptionCode.CANT_FIND_LESSON_BOOKING);
        }
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

    private Lesson findEnabledLessonByLessonId(Long lessonId) {
        return lessonRepository.findByLessonIdAndLessonState(lessonId, LessonState.Enabled)
                .orElseThrow(
                        () -> new RestApiException(ClientExceptionCode.CANT_FIND_LESSON)
                );
    }

    private void lessonBookingDuplicateCheck(LessonMember lessonMember, Lesson lesson) {
        if(lessonBookingRepository.existsByAndLessonMemberAndLesson(lessonMember, lesson)) {
            throw new RestApiException(ClientExceptionCode.ALREADY_BOOKED_LESSON);
        }
    }
}
