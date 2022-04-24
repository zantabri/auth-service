package com.zantabri.auth_service.model;

import com.fasterxml.jackson.annotation.JsonProperty;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.List;

@Entity
public class AccountDetails {

    public interface AccountDetailsOnBoardingValidation {}
    public interface AccountDetailsUpdatingValidation {}

    @Id
    @NotNull(groups = {AccountDetailsOnBoardingValidation.class, AccountDetailsUpdatingValidation.class})
    private String username;

    @NotNull(groups = {AccountDetailsOnBoardingValidation.class, AccountDetailsUpdatingValidation.class})
    @Email
    @Column(unique = true)
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull(groups = {AccountDetailsOnBoardingValidation.class})
    @Column(name = "password", nullable = false)
    @Size(min = 6)
    private String password;

    @NotNull(groups = {AccountDetailsOnBoardingValidation.class, AccountDetailsUpdatingValidation.class})
    @Pattern(regexp = "\\d{11,}")
    @Column(name = "telephone")
    private String telephone;

    @NotNull(groups = {AccountDetailsOnBoardingValidation.class, AccountDetailsUpdatingValidation.class})
    @NotBlank
    @Column(name = "first_name")
    private String firstName;

    @NotNull(groups = {AccountDetailsOnBoardingValidation.class, AccountDetailsUpdatingValidation.class})
    @NotBlank
    @Column(name = "last_name")
    private String lastName;

    @Column(name = "activated")
    private boolean activated;

    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private List<UserRole> authorities;

    @NotNull(groups = {AccountDetailsOnBoardingValidation.class, AccountDetailsUpdatingValidation.class})
    @Column(name = "organization_id", nullable = false)
    private long organizationId;

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

    public long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(long organization) {
        this.organizationId = organization;
    }

    @Override
    public String toString() {
        return "AccountDetails{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", telephone='" + telephone + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", activated=" + activated +
                ", organizationId=" + organizationId +
                '}';
    }
}
