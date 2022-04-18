package com.zantabri.auth_service.model;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long organizationId;

    @NotNull
    private String businessName;

    @NotNull
    private String address;

    @NotNull
    private String state;

    private LocalDate dateCreated;

    @NotNull
    private String contactPerson;

    @NotNull
    private String contactNumber;

    /**
     * the organization type
     *
     * @return
     */
    public abstract String getOrganizationType();

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long id) {
        this.organizationId = id;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }
}
