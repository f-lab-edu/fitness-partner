package com.fitnesspartner.repository;

import com.fitnesspartner.constants.InstructorState;
import com.fitnesspartner.domain.Instructor;
import com.fitnesspartner.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InstructorRepository extends JpaRepository<Instructor, Long> {
    Boolean existsByUsers(Users users);

    Optional<Instructor> findByUsersAndInstructorState(Users users, InstructorState instructorState);
}
