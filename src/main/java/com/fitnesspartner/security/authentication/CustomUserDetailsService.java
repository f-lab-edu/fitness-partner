package com.fitnesspartner.security.authentication;

import com.fitnesspartner.constants.UserState;
import com.fitnesspartner.domain.QUserRoles;
import com.fitnesspartner.domain.QUsers;
import com.fitnesspartner.domain.Users;
import com.fitnesspartner.exception.ClientExceptionCode;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public UserDetails loadUserByUsername(String username) {

        QUsers qUsers = QUsers.users;
        QUserRoles qUserRoles = QUserRoles.userRoles;

        Users user = Optional.ofNullable(
                jpaQueryFactory.selectFrom(qUsers)
                .leftJoin(qUsers.userRolesList, qUserRoles).fetchJoin()
                .where(qUsers.username.eq(username), qUsers.userState.eq(UserState.Enabled))
                .fetchOne()
        ).orElseThrow(
                () -> new UsernameNotFoundException(ClientExceptionCode.CANT_FIND_USER.getMessage())
        );

        return CustomUserDetails.builder()
                .users(user)
                .build();
    }
}
