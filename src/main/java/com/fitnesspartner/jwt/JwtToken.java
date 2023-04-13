package com.fitnesspartner.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum JwtToken {
    TOKEN_NAME("jwt")
    ;

    private final String tokenName;
}
