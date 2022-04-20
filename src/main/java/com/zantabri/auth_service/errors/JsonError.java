package com.zantabri.auth_service.errors;

public class JsonError {

    private String message;
    private String type;

    public JsonError(String message, String type) {
        this.message = message;
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public String getType() {
        return type;
    }
}
