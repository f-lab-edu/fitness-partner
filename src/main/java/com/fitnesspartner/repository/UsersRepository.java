package com.fitnesspartner.repository;

import com.fitnesspartner.constants.UserState;
import com.fitnesspartner.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
    Boolean existsByUsername(String username);

    Boolean existsByNickname(String nickname);

    Optional<Users> findByUsernameAndUserState(String username, UserState userState);
}
