package com.fitnesspartner.security;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum Roles {
    ADMIN("ROLE_ADMIN"),

    INSTRUCTOR("ROLE_INSTRUCTOR"),

    LESSON_MEMBER("ROLE_LESSON_MEMBER")
    ;

    private String roleName;
}
