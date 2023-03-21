package com.fitnesspartner.dto.users;

import com.fitnesspartner.constants.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.*;

@Getter
@Builder
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

    @Email(message = "이메일 포맷이 맞지 않습니다.")
    @NotBlank(message = "이메일을 입력해주세요.")
    private String email;

    @NotBlank(message = "전화번호를 작성해주세요.")
    @Pattern(regexp = "^(01[016789]{1})[0-9]{3,4}[0-9]{4}$", message = "전화번호 포맷에 맞게 입력해주세요.")
    private String phoneNumber;

    @NotNull(message = "성별을 입력해주세요.")
    private Gender gender;
}
