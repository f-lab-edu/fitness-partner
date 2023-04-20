package com.fitnesspartner.repository;

import com.fitnesspartner.domain.LessonMember;
import com.fitnesspartner.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LessonMemberRepository extends JpaRepository<LessonMember, Long> {
    Optional<LessonMember> findByUsers(Users users);
}
