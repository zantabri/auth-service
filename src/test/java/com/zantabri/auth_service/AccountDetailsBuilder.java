package com.zantabri.auth_service;

import com.zantabri.auth_service.model.AccountDetails;

import com.zantabri.auth_service.model.UserRole;

import java.util.List;

public class AccountDetailsBuilder {

    private AccountDetailsBuilder() {}

    public static AccountDetails from(String username, String firstName, String lastname, List<UserRole> userRoles,
                                      String email, boolean activated, String telephone, long organizationId) {

        AccountDetails accountDetails = new AccountDetails();
        accountDetails.setUsername(username);
        accountDetails.setActivated(activated);
        accountDetails.setAuthorities(userRoles);
        accountDetails.setEmail(email);
        accountDetails.setFirstName(firstName);
        accountDetails.setLastName(lastname);
        accountDetails.setOrganizationId(organizationId);
        accountDetails.setTelephone(telephone);

        return accountDetails;

    }

}
