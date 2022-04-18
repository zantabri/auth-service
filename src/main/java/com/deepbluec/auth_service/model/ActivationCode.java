package com.deepbluec.auth_service.model;

import org.springframework.lang.NonNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class ActivationCode {

    @NonNull
    @Column(name = "username")
    private String username;

    @Id
    private String code;

    @NonNull
    @Column(name = "expires")
    private LocalDateTime expires;

    public ActivationCode() {}

    public ActivationCode(String username, String code, LocalDateTime expires) {
        this.username = username;
        this.code = code;
        this.expires = expires;
    }

    @NonNull
    public String getUsername() {
        return username;
    }

    public void setUsername(@NonNull String username) {
        this.username = username;
    }

    @NonNull
    public String getCode() {
        return code;
    }

    public void setCode(@NonNull String code) {
        this.code = code;
    }

    @NonNull
    public LocalDateTime getExpires() {
        return expires;
    }

    public void setExpires(@NonNull LocalDateTime expires) {
        this.expires = expires;
    }
}
