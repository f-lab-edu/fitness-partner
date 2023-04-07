package com.fitnesspartner.repository;

import com.fitnesspartner.constants.LessonState;
import com.fitnesspartner.domain.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
    Optional<Lesson> findByLessonName(String lessonName);

    Optional<Lesson> findByLessonIdAndLessonState(Long lessonId, LessonState lessonState);
}
