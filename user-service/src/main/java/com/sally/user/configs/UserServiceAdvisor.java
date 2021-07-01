package com.sally.user.configs;

import com.sally.exceptions.CommonExceptionAdvisor;
import com.sally.exceptions.ErrorCode;
import com.sally.exceptions.ResultError;
import com.sally.exceptions.SallyException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@Order
@ControllerAdvice
public class UserServiceAdvisor extends CommonExceptionAdvisor {

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ResultError> handleAccessDenied(final HttpServletRequest request, final AccessDeniedException e) {
        return new ResponseEntity<>(ResultError.of(ErrorCode.ACCESS_DENIED), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public ResponseEntity<ResultError> handleAuthenticationException(final HttpServletRequest request,
                                                                     InternalAuthenticationServiceException exception) {
        final Throwable cause = exception.getCause();
        if (cause instanceof SallyException) {
            return handleSallyException(request, (SallyException) cause);
        }

        return handleAll(request, exception);
    }

}
