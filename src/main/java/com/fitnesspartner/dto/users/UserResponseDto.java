package com.fitnesspartner.dto.users;

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
    private int gender;
    private String phoneNumber;
    private String nickname;
    private boolean enabled;
}
