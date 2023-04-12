package com.fitnesspartner.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum JwtExceptionCode {
    TOKEN_NULL_OR_EMPTY(HttpStatus.BAD_REQUEST, 400, "토큰이 없거나 값이 비어있습니다."),
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, 400, "유효하지 않은 토큰입니다."),
    USERNAME_AND_AUTHENTICATION_NULL(HttpStatus.BAD_REQUEST, 400, "토큰에 유저 아이디가 없습니다.")
    ;

    private final HttpStatus httpStatus;
    private final int code;
    private final String message;
}
