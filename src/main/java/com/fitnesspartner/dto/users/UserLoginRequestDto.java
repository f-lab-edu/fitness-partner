package com.fitnesspartner.dto.users;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginRequestDto {

    @Size(min = 4, max = 15)
    @NotBlank(message = "유저 아이디를 입력해주세요.")
    private String username;

    @Size(min = 8, max = 15)
    @NotBlank(message = "패스워드를 입력해주세요.")
    private String password;
}
