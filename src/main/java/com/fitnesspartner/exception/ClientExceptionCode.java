package com.fitnesspartner.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ClientExceptionCode implements ErrorCode {

    ;

    private final HttpStatus httpStatus;
    private final int code;
    private final String message;
}
