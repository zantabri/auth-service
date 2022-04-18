package com.zantabri.auth_service.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnAuthorizedAccessException extends RuntimeException {

    public UnAuthorizedAccessException() {
        super("Unauthorized Access.");
    }

}
