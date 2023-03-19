package com.fitnesspartner.service;

import com.fitnesspartner.domain.Users;
import com.fitnesspartner.dto.users.*;
import com.fitnesspartner.exception.ClientExceptionCode;
import com.fitnesspartner.exception.RestApiException;
import com.fitnesspartner.repository.UsersRepository;
import com.fitnesspartner.utils.encryptor.Encryptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsersService {

    private final UsersRepository usersRepository;
    private final Encryptor encryptor;

    public String userSignup(UserSignupRequestDto userSignupRequestDto) {
        String username = userSignupRequestDto.getUsername();
        String rawPassword = userSignupRequestDto.getPassword();
        String nickname = userSignupRequestDto.getNickname();

        usernameDuplicateCheck(username);

        nicknameDuplicateCheck(nickname);

        String encryptedPassword = encryptor.hashPassword(rawPassword);

        Users users = Users.builder()
                .username(username)
                .name(userSignupRequestDto.getName())
                .password(encryptedPassword)
                .nickname(userSignupRequestDto.getNickname())
                .phoneNumber(userSignupRequestDto.getPhoneNumber())
                .email(userSignupRequestDto.getEmail())
                .gender(userSignupRequestDto.getGender())
                .enabled(true)
                .build();
        usersRepository.save(users);
        return "회원가입 성공";
    }


    public UserResponseDto userLogin(UserLoginRequestDto userLoginRequestDto) {
        return UserResponseDto.builder().build();
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
                .enabled(users.isEnabled())
                .build();
    }


    @Transactional
    public UserResponseDto userUpdate(String username, UserUpdateRequestDto userUpdateRequestDto) {
        Users users = findUserByUsernameIfExist(username);

        usernameDuplicateCheck(userUpdateRequestDto.getUsername());

        nicknameDuplicateCheck(userUpdateRequestDto.getNickname());

        String rawPassword = userUpdateRequestDto.getPassword();
        userUpdateRequestDto.setPassword(encryptor.hashPassword(rawPassword));

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

        if(!encryptor.isMatch(password, foundUserPassword)) {
            throw new RestApiException(ClientExceptionCode.PASSWORD_NOT_MATCH);
        }

        foundUsers.userDisable(false);

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
        return usersRepository.findByUsernameAndEnabled(username, true)
                .orElseThrow(
                        () -> new RestApiException(ClientExceptionCode.CANT_FIND_USER)
                );
    }
}
