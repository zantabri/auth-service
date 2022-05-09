package com.zantabri.auth_service.controllers;

import com.zantabri.auth_service.errors.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice(basePackages = "com.zantabri.auth_service.controllers")
public class ExceptionHandling {

    private Logger logger = LoggerFactory.getLogger(ExceptionHandling.class);
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    @ExceptionHandler(value = ResourceNotFoundException.class)
    public JsonError handleResourceNotFound(ResourceNotFoundException exception) {
        return toJsonError(exception);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseBody
    public JsonError handleBadCredentialsException(BadCredentialsException exception) {
        return toJsonError(exception);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ResourcePayloadValidationException.class)
    @ResponseBody
    public JsonError handleResourcePayloadValidationException(ResourcePayloadValidationException exception) {
        return toJsonError(exception);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = RuntimeException.class)
    @ResponseBody
    public JsonError handleSystemError(RuntimeException exception) {
        return toJsonError(exception);
    }


    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = ResourceValidationException.class)
    @ResponseBody
    public JsonError handleResourceViolationException(ResourceValidationException exception) {
        return toJsonError(exception);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(CredentialsExpiredException.class)
    @ResponseBody
    public JsonError handleCredentialsExpiredException(CredentialsExpiredException exception) {
        return toJsonError(exception);
    }

    private JsonError toJsonError(Exception e) {
        logger.error("an error occurred.", e);
        return new JsonError(e.getMessage(), e.getClass().getSimpleName());
    }

}
