package com.zantabri.auth_service.model;

import javax.persistence.Entity;

@Entity
public class Bank extends Organization {

    @Override
    public String getOrganizationType() {
        return "bank";
    }
}
