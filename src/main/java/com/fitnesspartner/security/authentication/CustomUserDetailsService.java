package com.fitnesspartner.security.authentication;

import com.fitnesspartner.constants.UserState;
import com.fitnesspartner.domain.Users;
import com.fitnesspartner.exception.ClientExceptionCode;
import com.fitnesspartner.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        Users users = usersRepository.findByUsernameAndUserState(username, UserState.Enabled)
                .orElseThrow(
                        () -> new RuntimeException(ClientExceptionCode.CANT_FIND_USER.getMessage())
                );

        return CustomUserDetails.builder()
                .users(users)
                .build();
    }
}
