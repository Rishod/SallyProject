package com.sally.exceptions;

import org.springframework.http.HttpStatus;

public class BadRequestException extends SallyException {
    public BadRequestException(ErrorCode errorCode) {
        super(HttpStatus.BAD_REQUEST, errorCode);
    }

    public BadRequestException(ErrorCode errorCode, Throwable cause) {
        super(HttpStatus.BAD_REQUEST, errorCode, cause);
    }
}
