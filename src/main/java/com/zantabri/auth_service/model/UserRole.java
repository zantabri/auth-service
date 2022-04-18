package com.zantabri.auth_service.model;

import com.sun.istack.NotNull;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Entity
public class UserRole implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "role")
    private String role;

    public UserRole() {
    }

    public UserRole(long id, String role) {
        this.id = id;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String getAuthority() {
        return this.role;
    }

}
