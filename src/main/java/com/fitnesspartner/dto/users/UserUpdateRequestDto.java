package com.fitnesspartner.dto.users;

import com.fitnesspartner.constants.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserUpdateRequestDto {

    @Size(min = 2, max = 13)
    private String name;

    @Size(max = 15)
    private String nickname;

    @Size(min = 8, max = 15)
    private String password;

    @Email(message = "이메일 포맷이 맞지 않습니다.")
    private String email;

    @Pattern(regexp = "^(01[016789]{1})[0-9]{3,4}[0-9]{4}$", message = "전화번호 포맷에 맞게 입력해주세요.")
    private String phoneNumber;

    private Gender gender;
}
