package com.fitnesspartner.dto.users;

import com.fitnesspartner.constants.Gender;
import com.fitnesspartner.constants.UserState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {

    private String username;
    private String name;
    private String email;
    private Gender gender;
    private String phoneNumber;
    private String nickname;
    private UserState userState;
}
