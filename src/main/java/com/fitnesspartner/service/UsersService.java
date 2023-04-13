package com.fitnesspartner.service;

import com.fitnesspartner.constants.UserState;
import com.fitnesspartner.domain.Users;
import com.fitnesspartner.dto.users.*;
import com.fitnesspartner.exception.ClientExceptionCode;
import com.fitnesspartner.exception.RestApiException;
import com.fitnesspartner.jwt.JwtService;
import com.fitnesspartner.repository.UsersRepository;
import com.fitnesspartner.security.authentication.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UsersService {

    private final UsersRepository usersRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    public String userSignup(UserSignupRequestDto userSignupRequestDto) {
        String username = userSignupRequestDto.getUsername();
        String rawPassword = userSignupRequestDto.getPassword();
        String nickname = userSignupRequestDto.getNickname();

        usernameDuplicateCheck(username);

        nicknameDuplicateCheck(nickname);

        String encryptedPassword = passwordEncoder.encode(rawPassword);

        Users users = Users.builder()
                .username(username)
                .name(userSignupRequestDto.getName())
                .password(encryptedPassword)
                .nickname(userSignupRequestDto.getNickname())
                .phoneNumber(userSignupRequestDto.getPhoneNumber())
                .email(userSignupRequestDto.getEmail())
                .gender(userSignupRequestDto.getGender())
                .userState(UserState.Enabled)
                .build();
        usersRepository.save(users);
        return "회원가입 성공";
    }

    public UserResponseDto userInfo(String username) {
        Users users = findUserByUsernameIfExist(username);

        return UserResponseDto.builder()
                .username(users.getUsername())
                .name(users.getName())
                .email(users.getEmail())
                .gender(users.getGender())
                .phoneNumber(users.getPhoneNumber())
                .nickname(users.getNickname())
                .userState(users.getUserState())
                .build();
    }


    @Transactional
    public UserResponseDto userUpdate(String username, UserUpdateRequestDto userUpdateRequestDto) {
        Users users = findUserByUsernameIfExist(username);

        usernameDuplicateCheck(userUpdateRequestDto.getUsername());

        nicknameDuplicateCheck(userUpdateRequestDto.getNickname());

        String rawPassword = userUpdateRequestDto.getPassword();
        userUpdateRequestDto.setPassword(passwordEncoder.encode(rawPassword));

        users.updateUser(userUpdateRequestDto);

        return UserResponseDto.builder()
                .username(users.getUsername())
                .nickname(users.getNickname())
                .email(users.getEmail())
                .gender(users.getGender())
                .phoneNumber(users.getPhoneNumber())
                .name(users.getName())
                .build();
    }

    @Transactional
    public String userDisable(UserDisableRequestDto userDisableRequestDto) {
        String username = userDisableRequestDto.getUsername();
        String password = userDisableRequestDto.getPassword();

        Users foundUsers = findUserByUsernameIfExist(username);

        String foundUserPassword = foundUsers.getPassword();

        if(!passwordEncoder.matches(password, foundUserPassword)) {
            throw new RestApiException(ClientExceptionCode.PASSWORD_NOT_MATCH);
        }

        foundUsers.userDisable(UserState.Disabled);

        return "비활성화 완료";
    }

    private void usernameDuplicateCheck(String username) {
        if(usersRepository.existsByUsername(username)) {
            throw new RestApiException(ClientExceptionCode.USERNAME_IS_DUPLICATE);
        }
    }

    private void nicknameDuplicateCheck(String nickname) {
        if(usersRepository.existsByNickname(nickname)) {
            throw new RestApiException(ClientExceptionCode.NICKNAME_IS_DUPLICATE);
        }
    }

    private Users findUserByUsernameIfExist(String username) {
        return usersRepository.findByUsernameAndUserState(username, UserState.Enabled)
                .orElseThrow(
                        () -> new RestApiException(ClientExceptionCode.CANT_FIND_USER)
                );
    }

    public String userLogin(UserLoginRequestDto requestDto) {
        String username = requestDto.getUsername();
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        username,
                        requestDto.getPassword()
                )
        );
        Users user = findUserByUsernameIfExist(username);
        CustomUserDetails userDetails = CustomUserDetails.builder()
                        .users(user)
                        .build();

        String tokenValue = jwtService.generateToken(userDetails);

        return tokenValue;
    }
}
