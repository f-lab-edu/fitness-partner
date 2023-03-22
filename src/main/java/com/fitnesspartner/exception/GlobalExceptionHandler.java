package com.fitnesspartner.exception;

import com.fitnesspartner.dto.common.ExceptionResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(RestApiException.class)
    protected ResponseEntity<ExceptionResponseDto> handleCustomRestApiException(RestApiException ex) {
        ErrorCode errorCode = ex.getErrorCode();

        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ExceptionResponseDto.builder()
                        .occurredTime(LocalDateTime.now())
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
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

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        BindingResult bindingResult = ex.getBindingResult();
        List<FieldError> fieldErrorList = bindingResult.getFieldErrors();

        StringBuilder sb = new StringBuilder();
        for(FieldError fieldError : fieldErrorList) {
            sb.append("{ [");
            sb.append(fieldError.getField());
            sb.append("](은)는 ");
            sb.append(fieldError.getDefaultMessage());
            sb.append(" } ");
        }

        return ResponseEntity
                .status(status)
                .body(ExceptionResponseDto.builder()
                        .occurredTime(LocalDateTime.now())
                        .code(status.value())
                        .message(sb.toString())
                        .build()
                );
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex,
                                                        HttpHeaders headers,
                                                        HttpStatus status,
                                                        WebRequest request) {

        return ResponseEntity
                .status(status)
                .body(ExceptionResponseDto.builder()
                        .occurredTime(LocalDateTime.now())
                        .code(status.value())
                        .message(ex.getMessage())
                        .build()
                );
    }
}