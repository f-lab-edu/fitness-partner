package com.fitnesspartner.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitnesspartner.annotation.EnableMockMvc;
import com.fitnesspartner.constants.Gender;
import com.fitnesspartner.dto.users.UserLoginRequestDto;
import com.fitnesspartner.dto.users.UserSignupRequestDto;
import com.fitnesspartner.repository.UsersRepository;
import com.fitnesspartner.service.UsersService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.FilterChain;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@EnableMockMvc
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class UsersControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    ObjectMapper objectMapper;

    @MockBean
    private UsersService userService;

    @MockBean
    private UsersRepository userRepository;

    private String userSignupUrl = "/users/signup";

    @Nested
    @DisplayName("성공 케이스")
    class 성공_케이스 {

        @Test
        @DisplayName("유저 회원가입")
        void 유저_회원가입() throws Exception {
            // given
            UserSignupRequestDto userSignupRequestDto = UserSignupRequestDto.builder()
                    .username("nahealth")
                    .name("김계란")
                    .nickname("나헬린")
                    .password("12345678")
                    .phoneNumber("01088889999")
                    .email("nahealth126@naver.com")
                    .gender(Gender.MALE)
                    .build();

            String content = objectMapper.writeValueAsString(userSignupRequestDto);

            // when, then
            mockMvc.perform(post(userSignupUrl)
                    .servletPath(userSignupUrl)
                    .content(content)
                    .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk());
        }

        @Test
        @DisplayName("유저 로그인 / 토큰 발급")
        void 유저_로그인() throws Exception {
            // given
            String loginUrl = "/users/login";
            UserLoginRequestDto userLoginRequestDto = UserLoginRequestDto.builder()
                    .username("nahealth")
                    .password("12345678")
                    .build();

            String content = objectMapper.writeValueAsString(userLoginRequestDto);

            // when, then
            mockMvc.perform(post(loginUrl)
                    .servletPath(loginUrl)
                    .content(content)
                    .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("실패 케이스")
    class 실패_케이스 {


        @Nested
        @DisplayName("유저 회원가입 실패 케이스")
        class 유저_회원가입_실패_케이스 {

            @Test
            @DisplayName("이메일 미입력")
            void 이메일_미입력() throws Exception {
                // given
                UserSignupRequestDto userSignupRequestDto = UserSignupRequestDto.builder()
                        .username("nahealth")
                        .name("김계란")
                        .nickname("나헬린")
                        .password("12345678")
                        .phoneNumber("01088889999")
                        .gender(Gender.MALE)
                        .build();

                String content = objectMapper.writeValueAsString(userSignupRequestDto);

                // when, then
                mockMvc.perform(post(userSignupUrl)
                                .content(content)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isBadRequest());
            }

            @Test
            @DisplayName("이메일 포맷 미준수")
            void 이메일_포맷_미준수() throws Exception {
                // given
                String email = "nahealth123@@@@Naver.com";
                UserSignupRequestDto userSignupRequestDto = UserSignupRequestDto.builder()
                        .username("nahealth")
                        .name("김계란")
                        .nickname("나헬린")
                        .password("12345678")
                        .email(email)
                        .phoneNumber("01088889999")
                        .gender(Gender.MALE)
                        .build();

                String content = objectMapper.writeValueAsString(userSignupRequestDto);

                // when, then
                mockMvc.perform(post(userSignupUrl)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isBadRequest());
            }

            @Test
            @DisplayName("이메일 빈 문자열 입력")
            void 이메일_빈문자열() throws Exception {
                // given
                String email = "";
                UserSignupRequestDto userSignupRequestDto = UserSignupRequestDto.builder()
                        .username("nahealth")
                        .name("김계란")
                        .nickname("나헬린")
                        .password("12345678")
                        .email(email)
                        .phoneNumber("01088889999")
                        .gender(Gender.MALE)
                        .build();

                String content = objectMapper.writeValueAsString(userSignupRequestDto);

                // when, then
                mockMvc.perform(post(userSignupUrl)
                                .content(content)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isBadRequest());
            }

            @Test
            @DisplayName("이메일 null 입력")
            void 이메일_null_입력() throws Exception {
                // given
                String email = null;
                UserSignupRequestDto userSignupRequestDto = UserSignupRequestDto.builder()
                        .username("nahealth")
                        .name("김계란")
                        .nickname("나헬린")
                        .password("12345678")
                        .email(email)
                        .phoneNumber("01088889999")
                        .gender(Gender.MALE)
                        .build();

                String content = objectMapper.writeValueAsString(userSignupRequestDto);

                // when, then
                mockMvc.perform(post(userSignupUrl)
                                .content(content)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isBadRequest());
            }


            @Test
            @DisplayName("전화번호 빈 문자열 입력")
            void 전화번호_빈문자열() throws Exception {
                // given
                String phoneNumber = "";
                UserSignupRequestDto userSignupRequestDto = UserSignupRequestDto.builder()
                        .username("nahealth")
                        .name("김계란")
                        .nickname("나헬린")
                        .password("12345678")
                        .email("nahealth126@naver.com")
                        .phoneNumber(phoneNumber)
                        .gender(Gender.MALE)
                        .build();

                String content = objectMapper.writeValueAsString(userSignupRequestDto);

                // when, then
                mockMvc.perform(post(userSignupUrl)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isBadRequest());
            }

            @Test
            @DisplayName("전화번호 null 입력")
            void 전화번호_null_입력() throws Exception {
                // given
                String phoneNumber = null;
                UserSignupRequestDto userSignupRequestDto = UserSignupRequestDto.builder()
                        .username("nahealth")
                        .name("김계란")
                        .nickname("나헬린")
                        .password("12345678")
                        .email("nahealth126@naver.com")
                        .phoneNumber(phoneNumber)
                        .gender(Gender.MALE)
                        .build();

                String content = objectMapper.writeValueAsString(userSignupRequestDto);

                // when, then
                mockMvc.perform(post(userSignupUrl)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isBadRequest());
            }

            @Test
            @DisplayName("전화번호 포맷 미준수")
            void 전화번호_포맷_미준수() throws Exception {
                // given
                String phoneNumber = "핸드폰번호가아님";
                UserSignupRequestDto userSignupRequestDto = UserSignupRequestDto.builder()
                        .username("nahealth")
                        .name("김계란")
                        .nickname("나헬린")
                        .password("12345678")
                        .email("nahealth126@naver.com")
                        .phoneNumber(phoneNumber)
                        .gender(Gender.MALE)
                        .build();

                String content = objectMapper.writeValueAsString(userSignupRequestDto);

                // when, then
                mockMvc.perform(post(userSignupUrl)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isBadRequest());
            }

            @Test
            @DisplayName("전화번호 포맷 미준수2")
            void 전화번호_포맷_미준수2() throws Exception {
                // given
                String phoneNumber = "11122223333";
                UserSignupRequestDto userSignupRequestDto = UserSignupRequestDto.builder()
                        .username("nahealth")
                        .name("김계란")
                        .nickname("나헬린")
                        .password("12345678")
                        .email("nahealth126@naver.com")
                        .phoneNumber(phoneNumber)
                        .gender(Gender.MALE)
                        .build();

                String content = objectMapper.writeValueAsString(userSignupRequestDto);

                // when, then
                mockMvc.perform(post(userSignupUrl)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isBadRequest());
            }
        }
    }
}