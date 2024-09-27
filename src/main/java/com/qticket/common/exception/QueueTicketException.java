package com.qticket.common.exception;

import lombok.Getter;

@Getter
public class QueueTicketException extends RuntimeException {
    private final ErrorCode errorCode;
    public QueueTicketException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
