package com.zantabri.auth_service.errors;

/**
 *
 */
public class ResourceValidationException extends RuntimeException {

    public ResourceValidationException(String message) {
        super(message);
    }
}
