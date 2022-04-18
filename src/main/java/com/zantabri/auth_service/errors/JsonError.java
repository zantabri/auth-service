package com.zantabri.auth_service.errors;

public class JsonError {

    String message;
    String type;

    public JsonError(String message, String type) {
        this.message = message;
        this.type = type;
    }
}
