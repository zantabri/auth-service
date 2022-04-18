package com.deepbluec.auth_service.security;

public class JWTAuthenticationResultDTO extends AuthenticationResultDTO {

    final String token;

    public JWTAuthenticationResultDTO(String username, String token) {
        super(username);
        this.token  = token;
    }

}
