package com.deepbluec.auth_service.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.time.LocalDate;


@Entity
public class PTSPOrganization extends Organization {

    @Column(nullable = false)
    private LocalDate licenseIssueDate;

    @Column(nullable = false)
    private LocalDate LicenseExpiryDate;

    public LocalDate getLicenseIssueDate() {
        return licenseIssueDate;
    }

    public void setLicenseIssueDate(LocalDate licenseIssueDate) {
        this.licenseIssueDate = licenseIssueDate;
    }

    public LocalDate getLicenseExpiryDate() {
        return LicenseExpiryDate;
    }

    public void setLicenseExpiryDate(LocalDate licenseExpiryDate) {
        LicenseExpiryDate = licenseExpiryDate;
    }

    @Override
    public String getOrganizationType() {
        return "ptsp";
    }

}
