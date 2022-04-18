package com.zantabri.auth_service.errors;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice(basePackages = "com.deepbluec.ptsp.controllers")
public class ExceptionHandling {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = ResourceNotFoundException.class)
    public JsonError handleResourceNotFound(ResourceNotFoundException exception) {
        return toJsonError(exception);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadCredentialsException.class)
    public JsonError handleBadCredentialsException(BadCredentialsException exception) {
        return toJsonError(exception);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = RuntimeException.class)
    public JsonError handleSystemError(RuntimeException exception) {
        return toJsonError(exception);
    }

    private JsonError toJsonError(Exception e) {
        return new JsonError(e.getMessage(), e.getClass().getSimpleName());
    }

}
