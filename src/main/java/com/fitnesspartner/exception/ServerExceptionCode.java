package com.fitnesspartner.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ServerExceptionCode implements ErrorCode {
    ILLEGAL_ARGUMENT_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR,500, "메소드에 불법적이거나 부적절한 인수가 전달되었습니다."),
    NULL_POINTER_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR,500, "[NPE]null 상태인 변수에서 내용을 참조할려고 합니다."),
    NO_SUCH_ELEMENT_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, 500, "요청 중인 요소(Element)가 존재하지 않습니다."),
    INDEX_OUT_OF_BOUNDS_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, 500, "인덱스 범위를 벗어났습니다."),
    CLASS_CAST_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, 500, "인스턴스가 아닌 하위 클래스로 객체를 캐스팅 시도했습니다.")
    ;

    private final HttpStatus httpStatus;
    private final int code;
    private final String message;
}
