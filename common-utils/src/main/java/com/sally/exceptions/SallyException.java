package com.sally.exceptions;

import static java.util.Optional.ofNullable;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class SallyException extends RuntimeException {
    private HttpStatus httpStatus;
    private String errorCode;
    private String message;

    public SallyException(HttpStatus httpStatus, ErrorCode errorCode) {
        this(httpStatus, errorCode, null, null);
    }

    public SallyException(HttpStatus httpStatus, ErrorCode errorCode, String customMessage) {
        this(httpStatus, errorCode, customMessage, null);
    }

    public SallyException(HttpStatus httpStatus, ErrorCode errorCode, Throwable cause) {
        this(httpStatus, errorCode, null, cause);
    }

    public SallyException(HttpStatus httpStatus, ErrorCode errorCode, String customMessage, Throwable cause) {
        super(cause);
        this.httpStatus = httpStatus;
        this.errorCode = errorCode.name().toLowerCase();
        this.message = ofNullable(customMessage).orElse(errorCode.getMessage());
    }
}
