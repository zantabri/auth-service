package com.zantabri.auth_service.model;

import org.springframework.security.core.GrantedAuthority;

public class Permission implements GrantedAuthority {

    private final String authority;

    private Permission(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return this.authority;
    }


}
