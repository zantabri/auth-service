package com.zantabri.auth_service.security;

public class JWTAuthenticationResultDTO extends AuthenticationResultDTO {

    final private String token;

    public JWTAuthenticationResultDTO(String username, String token) {
        super(username);
        this.token  = token;
    }

    public String getToken() {
        return token;
    }
}
