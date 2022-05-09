package com.zantabri.auth_service.security;

import com.zantabri.auth_service.model.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class JWTUserDetails implements UserDetails {

    private String userName;
    private long organizationId;
    private boolean accountNonExpired = true;
    private boolean accountNonLocked = true;
    private boolean credentialsNonExpired =  true;
    private boolean enabled;
    private List<UserRole> authorities;

    public JWTUserDetails(String userName, long organizationId, boolean accountNonExpired, boolean accountNonLocked, boolean credentialsNonExpired, boolean enabled, List<UserRole> authorities) {
        this.userName = userName;
        this.organizationId = organizationId;
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
        this.enabled = enabled;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public long getOrganizationId() {
        return organizationId;
    }

}
