package com.sally.exceptions;


import org.springframework.http.HttpStatus;

public class NotFoundException extends SallyException {
    public NotFoundException(ErrorCode errorCode) {
        super(HttpStatus.NOT_FOUND, errorCode);
    }

    public NotFoundException(ErrorCode errorCode, Throwable cause) {
        super(HttpStatus.NOT_FOUND, errorCode, cause);
    }
}
