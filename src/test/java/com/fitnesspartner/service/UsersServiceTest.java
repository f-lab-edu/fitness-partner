package com.fitnesspartner.service;

import com.fitnesspartner.constants.Gender;
import com.fitnesspartner.constants.UserState;
import com.fitnesspartner.domain.Users;
import com.fitnesspartner.dto.users.UserDisableRequestDto;
import com.fitnesspartner.dto.users.UserResponseDto;
import com.fitnesspartner.dto.users.UserSignupRequestDto;
import com.fitnesspartner.dto.users.UserUpdateRequestDto;
import com.fitnesspartner.repository.UsersRepository;
import com.fitnesspartner.utils.encryptor.Encryptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class UsersServiceTest {

    @Autowired
    UsersService usersService;

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    Encryptor encryptor;

    @Nested
    @DisplayName("서비스레이어 성공케이스")
    class 성공케이스 {
        private String username = "nahealth";
        private String password =  "12345678";
        private UserSignupRequestDto userSignupRequestDto;

        @BeforeEach
        void BeforeEach() {
            userSignupRequestDto = new UserSignupRequestDto(
                    username,
                    "김계란",
                    "나헬린",
                    password,
                    "nahealth123@Naver.com",
                    "01088889999",
                    Gender.MALE
            );

            usersService.userSignup(userSignupRequestDto);
        }

        @Test
        @DisplayName("유저 회원가입")
        void 유저_회원가입() {
            // when
            Users foundUsers = usersRepository.findByUsernameAndUserState(userSignupRequestDto.getUsername(), UserState.Enabled).get();

            // then
            assertEquals(foundUsers.getUsername(), userSignupRequestDto.getUsername());
            assertEquals(foundUsers.getName(), userSignupRequestDto.getName());
            assertEquals(foundUsers.getNickname(), userSignupRequestDto.getNickname());
            assertEquals(foundUsers.getGender(), userSignupRequestDto.getGender());
            assertEquals(foundUsers.getPhoneNumber(), userSignupRequestDto.getPhoneNumber());
            assertEquals(foundUsers.getEmail(), userSignupRequestDto.getEmail());
        }


        @Test
        @DisplayName("유저 비활성화")
        void 유저_비활성화() {
            // given
            String password = "12345678";
            UserDisableRequestDto userDisableRequestDto = new UserDisableRequestDto(
                    username,
                    password
            );

            // when
            usersService.userDisable(userDisableRequestDto);
            Users foundUser = usersRepository.findByUsernameAndUserState(username, UserState.Enabled).get();

            // then
            assertEquals(foundUser.getUserState(), UserState.Disabled);
        }

        @Test
        @DisplayName("유저 정보조회")
        void 유저_정보조회() {
            // when
            UserResponseDto userResponseDto = usersService.userInfo(username);

            // then
            assertEquals(userResponseDto.getUsername(), username);
        }

        @Test
        @DisplayName("유저 정보수정")
        void 유저_정보수정() {
            // given
            UserUpdateRequestDto userUpdateRequestDto = new UserUpdateRequestDto(
                    "nahealthh",
                    "김계란2",
                    "나헬린2",
                    "123456789",
                    "nahealth124@daum.net",
                    "010222233333",
                    Gender.MALE
            );

            // when
            UserResponseDto userResponseDto = usersService.userUpdate(username, userUpdateRequestDto);

            // then
            assertEquals(userResponseDto.getUsername(), userUpdateRequestDto.getUsername());
            assertEquals(userResponseDto.getName(), userUpdateRequestDto.getName());
            assertEquals(userResponseDto.getNickname(), userResponseDto.getNickname());
            assertEquals(userResponseDto.getEmail(), userResponseDto.getEmail());
            assertEquals(userResponseDto.getPhoneNumber(), userResponseDto.getPhoneNumber());
            assertEquals(userResponseDto.getGender(), userResponseDto.getGender());
        }
    }
}