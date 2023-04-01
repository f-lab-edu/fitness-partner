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
    PASSWORD_NOT_MATCH(HttpStatus.BAD_REQUEST, 400, "비밀번호가 틀립니다."),

    // Instructor
    USER_ALREADY_INSTRUCTOR(HttpStatus.BAD_REQUEST, 400, "유저가 이미 강사입니다."),
    CANT_FIND_INSTRUCTOR(HttpStatus.BAD_REQUEST, 400, "강사를 찾을 수 없습니다."),

    // Lesson
    CANT_FIND_LESSON(HttpStatus.BAD_REQUEST, 400, "레슨을 찾을 수 없습니다."),
    NOT_LESSON_INSTRUCTOR(HttpStatus.BAD_REQUEST, 400, "레슨 강사가 아닙니다."),
    START_TIME_GREATER_THAN_END_TIME(HttpStatus.BAD_REQUEST, 400, "시작 시간이 끝나는 시간보다 큽니다."),
    LESSON_NAME_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, 400, "이미 있는 레슨명입니다.")
    ;




    private final HttpStatus httpStatus;
    private final int code;
    private final String message;
}
