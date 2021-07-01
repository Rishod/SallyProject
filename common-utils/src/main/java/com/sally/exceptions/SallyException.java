package com.sally.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class SallyException extends RuntimeException {
    private HttpStatus httpStatus;
    private String errorCode;
    private String message;

    public SallyException(HttpStatus httpStatus, ErrorCode errorCode) {
        this(httpStatus, errorCode, null);
    }

    public SallyException(HttpStatus httpStatus, ErrorCode errorCode, Throwable cause) {
        super(cause);
        this.httpStatus = httpStatus;
        this.errorCode = errorCode.name().toLowerCase();
        this.message = errorCode.getMessage();
    }
}
