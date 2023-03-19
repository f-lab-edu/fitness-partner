package com.fitnesspartner.dto.users;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.*;

@Getter
@AllArgsConstructor
public class UserSignupRequestDto {

    @Size(min = 4, max = 15)
    @NotBlank(message = "유저 아이디를 입력해주세요.")
    private String username;

    @Size(min = 2, max = 13)
    @NotBlank(message = "이름을 입력해주세요.")
    private String name;

    @Size(max = 15)
    @NotBlank(message = "닉네임을 입력해주세요.")
    private String nickname;

    @Size(min = 8, max = 15)
    @NotBlank(message = "패스워드를 입력해주세요.")
    private String password;

    @Email
    @NotBlank(message = "이메일을 입력해주세요.")
    private String email;

    @NotBlank(message = "전화번호를 작성해주세요.")
    @Pattern(regexp = "[0-9]{10,11}", message = "10~11자리의 숫자만 입력가능합니다")
    private String phoneNumber;

    @NotNull(message="0(남자) 혹은 1(여자)의 정수를 입력해주세요")
    @Min(value = 0, message = "0(남자) 혹은 1(여자)의 정수를 입력해주세요")
    @Max(value = 1, message = "0(남자) 혹은 1(여자)의 정수를 입력해주세요")
    private Integer gender;
}
