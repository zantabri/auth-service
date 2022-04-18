package com.deepbluec.auth_service.security;

import com.deepbluec.auth_service.model.AccountDetails;
import com.deepbluec.auth_service.model.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class UserDetailsImpl implements UserDetails {

    private String userName;
    private String password;
    private long organizationId;
    private boolean accountNonExpired = true;
    private boolean accountNonLocked = true;
    private boolean credentialsNonExpired =  true;
    private boolean enabled;
    private List<UserRole> authorities;
    private String organizationType;

    private UserDetailsImpl() {}

    public static UserDetailsImpl empty() {
        return new UserDetailsImpl();
    }

    public UserDetailsImpl(AccountDetails accountDetails) {
        this.password = accountDetails.getPassword();
        this.userName = accountDetails.getUsername();
        this.enabled = accountDetails.isActivated();
        this.authorities = accountDetails.getAuthorities();
        this.organizationId = accountDetails.getOrganization().getOrganizationId();
        this.organizationType = accountDetails.getOrganization().getOrganizationType();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
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

    public String getUserName() {
        return userName;
    }

    public long getOrganizationId() {
        return organizationId;
    }

    public String getOrganizationType() {
        return organizationType;
    }

    @Override
    public String toString() {
        return "AuthenticationDetails{" +
                "userName='" + userName + '\'' +
                ", organizationId=" + organizationId +
                ", accountNonExpired=" + accountNonExpired +
                ", accountNonLocked=" + accountNonLocked +
                ", credentialsNonExpired=" + credentialsNonExpired +
                ", enabled=" + enabled +
                ", organizationType='" + organizationType + '\'' +
                '}';
    }
}
