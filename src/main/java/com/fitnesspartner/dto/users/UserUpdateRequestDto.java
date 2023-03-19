package com.fitnesspartner.dto.users;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
@AllArgsConstructor
public class UserUpdateRequestDto {

    @Size(min = 4, max = 15)
    private String username;

    @Size(min = 2, max = 13)
    private String name;

    @Size(max = 15)
    private String nickname;

    @Size(min = 8, max = 15)
    private String password;

    @Email
    private String email;

    @Pattern(regexp = "[0-9]{10,11}", message = "10~11자리의 숫자만 입력가능합니다")
    private String phoneNumber;

    @Min(value = 0, message = "0(남자) 혹은 1(여자)의 정수를 입력해주세요")
    @Max(value = 1, message = "0(남자) 혹은 1(여자)의 정수를 입력해주세요")
    private Integer gender;
}
