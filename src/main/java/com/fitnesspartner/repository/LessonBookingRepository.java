package com.fitnesspartner.repository;

import com.fitnesspartner.domain.Lesson;
import com.fitnesspartner.domain.LessonBooking;
import com.fitnesspartner.domain.LessonMember;
import com.fitnesspartner.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LessonBookingRepository extends JpaRepository<LessonBooking, Long> {
    Boolean existsByAndLessonMemberAndLesson(LessonMember lessonMember, Lesson lesson);

    List<LessonBooking> findAllByLessonMember(LessonMember lessonMember);
}
