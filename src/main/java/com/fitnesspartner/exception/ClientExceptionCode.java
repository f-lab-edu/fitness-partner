package com.fitnesspartner.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ClientExceptionCode implements ErrorCode {
    USERNAME_IS_DUPLICATE(HttpStatus.BAD_REQUEST, 400, "유저아이디 중복"),
    NICKNAME_IS_DUPLICATE(HttpStatus.BAD_REQUEST, 400, "닉네임 중복"),
    LOGIN_FAILED(HttpStatus.BAD_REQUEST, 400, "로그인에 실패했습니다."),
    CANT_FIND_USER(HttpStatus.BAD_REQUEST, 400, "유저를 찾을 수 없습니다."),
    PASSWORD_NOT_MATCH(HttpStatus.BAD_REQUEST, 400, "비밀번호가 틀립니다.")
    ;



    private final HttpStatus httpStatus;
    private final int code;
    private final String message;
}
