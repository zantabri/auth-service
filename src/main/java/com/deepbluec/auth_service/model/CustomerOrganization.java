package com.deepbluec.auth_service.model;

import javax.persistence.*;


@Entity
public class CustomerOrganization extends Organization {

    @ManyToOne
    @JoinColumn(name = "parent_organization_id")
    private CustomerOrganization parentOrganization;

    @Enumerated(EnumType.STRING)
    private CustomerBusinessType businessType;

    public CustomerOrganization getParentOrganization() {
        return parentOrganization;
    }

    public void setParentOrganization(CustomerOrganization parentOrganization) {
        this.parentOrganization = parentOrganization;
    }

    public CustomerBusinessType getBusinessType() {
        return businessType;
    }

    public void setBusinessType(CustomerBusinessType businessType) {
        this.businessType = businessType;
    }

    @Override
    public String getOrganizationType() {
        return "customer";
    }
}
