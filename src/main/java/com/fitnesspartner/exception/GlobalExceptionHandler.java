package com.fitnesspartner.exception;

import com.fitnesspartner.dto.common.ExceptionResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(RestApiException.class)
    protected ResponseEntity<ExceptionResponseDto> handleCustomRestApiException(RestApiException ex) {
        ServerExceptionCode serverExceptionCode = ex.getServerExceptionCode();

        return ResponseEntity
                .status(serverExceptionCode.getHttpStatus())
                .body(ExceptionResponseDto.builder()
                        .occurredTime(LocalDateTime.now())
                        .code(serverExceptionCode.getCode())
                        .message(serverExceptionCode.getMessage())
                        .build()
                );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<ExceptionResponseDto> handleIllegalArgumentException(IllegalArgumentException ex) {
        ServerExceptionCode illegalArgumentException = ServerExceptionCode.ILLEGAL_ARGUMENT_EXCEPTION;

        return ResponseEntity
                .status(illegalArgumentException.getHttpStatus())
                .body(ExceptionResponseDto.builder()
                        .occurredTime(LocalDateTime.now())
                        .code(illegalArgumentException.getCode())
                        .message(ex.getMessage())
                        .build()
                );
    }

    @ExceptionHandler(NoSuchElementException.class)
    protected ResponseEntity<ExceptionResponseDto> handleNoSuchElementException(NoSuchElementException ex) {
        ServerExceptionCode noSuchElementException = ServerExceptionCode.NO_SUCH_ELEMENT_EXCEPTION;

        return ResponseEntity
                .status(noSuchElementException.getHttpStatus())
                .body(ExceptionResponseDto.builder()
                        .occurredTime(LocalDateTime.now())
                        .code(noSuchElementException.getCode())
                        .message(ex.getMessage())
                        .build()
                );
    }

    @ExceptionHandler(NullPointerException.class)
    protected ResponseEntity<ExceptionResponseDto> handleNullPointerException(NullPointerException ex) {
        ServerExceptionCode nullPointerException = ServerExceptionCode.NULL_POINTER_EXCEPTION;

        return ResponseEntity
                .status(nullPointerException.getHttpStatus())
                .body(ExceptionResponseDto.builder()
                        .occurredTime(LocalDateTime.now())
                        .code(nullPointerException.getCode())
                        .message(ex.getMessage())
                        .build()

                );
    }

    @ExceptionHandler(IndexOutOfBoundsException.class)
    protected ResponseEntity<ExceptionResponseDto> handleIndexOutOfBoundsException(IndexOutOfBoundsException ex) {
        ServerExceptionCode indexOutOfBoundsException = ServerExceptionCode.INDEX_OUT_OF_BOUNDS_EXCEPTION;

        return ResponseEntity
                .status(indexOutOfBoundsException.getHttpStatus())
                .body(ExceptionResponseDto.builder()
                        .occurredTime(LocalDateTime.now())
                        .code(indexOutOfBoundsException.getCode())
                        .message(ex.getMessage())
                        .build()
                );
    }

    @ExceptionHandler(ClassCastException.class)
    protected ResponseEntity<ExceptionResponseDto> handleClassCastException(ClassCastException ex) {
        ServerExceptionCode classCastException = ServerExceptionCode.CLASS_CAST_EXCEPTION;

        return ResponseEntity
                .status(classCastException.getHttpStatus())
                .body(ExceptionResponseDto.builder()
                        .occurredTime(LocalDateTime.now())
                        .code(classCastException.getCode())
                        .message(ex.getMessage())
                        .build()
                );
    }
}