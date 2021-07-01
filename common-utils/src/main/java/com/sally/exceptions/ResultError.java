package com.sally.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultError {
    private String errorCode;
    private String message;

    public static ResultError of(ErrorCode errorCode) {
        return new ResultError(errorCode.name().toLowerCase(), errorCode.getMessage());
    }

    public static ResultError of(SallyException exception) {
        return new ResultError(exception.getErrorCode(), exception.getMessage());
    }
}
