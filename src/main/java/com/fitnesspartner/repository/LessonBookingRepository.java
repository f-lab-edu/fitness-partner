package com.fitnesspartner.repository;

import com.fitnesspartner.domain.Lesson;
import com.fitnesspartner.domain.LessonBooking;
import com.fitnesspartner.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LessonBookingRepository extends JpaRepository<LessonBooking, Long> {
    Boolean existsByUsersAndLesson(Users users, Lesson lesson);

    List<LessonBooking> findByUsers(Users users);
}
