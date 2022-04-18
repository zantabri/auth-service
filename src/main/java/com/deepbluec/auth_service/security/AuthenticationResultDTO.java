package com.deepbluec.auth_service.security;

/**
 * Captures the result of an authentication Request
 */
public abstract class AuthenticationResultDTO {

    private String username;

    public AuthenticationResultDTO(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }


}
