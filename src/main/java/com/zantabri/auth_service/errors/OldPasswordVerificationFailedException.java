package com.zantabri.auth_service.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class OldPasswordVerificationFailedException extends RuntimeException {

    public OldPasswordVerificationFailedException(String message) {
        super(message);
    }

}
