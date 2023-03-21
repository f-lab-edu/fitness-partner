package com.fitnesspartner.dto.users;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
public class UserLoginRequestDto {

    @Size(min = 4, max = 15)
    @NotBlank(message = "Users Id를 입력해주세요.")
    private String username;

    @Size(min = 8, max = 15)
    @NotBlank(message = "패스워드를 입력해주세요.")
    private String password;
}
