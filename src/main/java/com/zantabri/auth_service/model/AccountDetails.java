package com.zantabri.auth_service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;


import javax.persistence.*;
import java.util.List;

@Entity
public class AccountDetails {

    @Id
    private String username;

    @NotNull
    @Column(unique = true)
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull
    @Column(name = "password")
    private String password;

    @NotNull
    @Column(name = "telephone")
    private String telephone;

    @NotNull
    @Column(name = "first_name")
    private String firstName;

    @NotNull
    @Column(name = "last_name")
    private String lastName;

    @NotNull
    @Column(name = "activated")
    private boolean activated;

    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private List<UserRole> authorities;

    @NotNull
    @JoinColumn(name = "organization_id", nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private Organization organization;

    public AccountDetails() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public List<UserRole> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<UserRole> authorities) {
        this.authorities = authorities;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }
}
