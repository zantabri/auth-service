package com.deepbluec.auth_service.security;

/**
 * Captures the payload of an authentication request.
 */
public class AuthenticationCredentialsDTO {

    private final String username;
    private final String password;

    public AuthenticationCredentialsDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

}
