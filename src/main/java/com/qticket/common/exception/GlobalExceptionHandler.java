package com.qticket.common.exception;

import com.qticket.common.dto.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(QueueTicketException.class)
    public ResponseEntity<ResponseDto<String>> handleQueueTicketException(QueueTicketException e) {
        ErrorCode errorCode = e.getErrorCode();
        HttpStatus status = errorCode.getStatus();
        String message = errorCode.getMessage();
        return ResponseEntity
                .status(status)
                .body(ResponseDto.error(message));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDto<String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {

        ObjectError first = e.getBindingResult().getAllErrors().stream().findFirst().get();
        String message = first.getDefaultMessage();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ResponseDto.error(message));
    }
}
