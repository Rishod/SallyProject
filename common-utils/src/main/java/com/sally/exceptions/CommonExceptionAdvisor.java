package com.sally.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;

@Slf4j
public abstract class CommonExceptionAdvisor implements ResponseBodyAdvice<Object> {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResultError> handleAll(final HttpServletRequest request, final Throwable e) {
        log.error("Internal server error", e);
        return new ResponseEntity<>(ResultError.of(ErrorCode.INTERNAL_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(SallyException.class)
    public ResponseEntity<ResultError> handleSallyException(final HttpServletRequest request, final SallyException e) {
        final HttpStatus httpStatus = e.getHttpStatus();
        if (httpStatus.is5xxServerError()) {
            log.error("Error: ", e);
        }

        return new ResponseEntity<>(ResultError.of(e), httpStatus);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ResultError> handleUnsupportedMethod(final HttpServletRequest request, final HttpRequestMethodNotSupportedException e) {
        return new ResponseEntity<>(ResultError.of(ErrorCode.UNSUPPORTED_METHOD), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ResultError> handleUnsupportedMethod(final HttpServletRequest request, final HttpMessageNotReadableException e) {
        return new ResponseEntity<>(ResultError.of(ErrorCode.BAD_REQUEST_BODY), HttpStatus.BAD_REQUEST);
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        return false;
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType,
                                  Class<? extends HttpMessageConverter<?>> aClass,
                                  ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        return null;
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ResultError> handleAccessDenied(final HttpServletRequest request, final AccessDeniedException e) {
        return new ResponseEntity<>(ResultError.of(ErrorCode.ACCESS_DENIED), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResultError> handleInvalidArguments(final HttpServletRequest request, MethodArgumentNotValidException e) {
        return new ResponseEntity<>(ResultError.of(ErrorCode.BAD_REQUEST_BODY), HttpStatus.BAD_REQUEST);
    }
}
