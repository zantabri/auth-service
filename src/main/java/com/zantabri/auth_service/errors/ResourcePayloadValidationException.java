package com.zantabri.auth_service.errors;

public class ResourcePayloadValidationException extends RuntimeException {

    private Object payload;
    public ResourcePayloadValidationException(Object object, String message) {
        super(message);
        this.payload = object;
    }

    public Object getPayload() {
        return payload;
    }

}
