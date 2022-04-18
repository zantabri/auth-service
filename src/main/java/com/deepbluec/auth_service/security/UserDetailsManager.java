package com.deepbluec.auth_service.security;

import com.deepbluec.auth_service.errors.OldPasswordVerificationFailedException;
import com.deepbluec.auth_service.model.AccountDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserDetailsManager extends UserDetailsService {

    /**
     * create an account
     * @param accountDetails
     */
    AccountDetails createAccount(AccountDetails accountDetails);

    /**
     * update an account
     * @param accountDetails
     */
    AccountDetails updateAccount(AccountDetails accountDetails);

    /**
     * delete the account that own's the provided username
     * @param username
     */
    void deleteAccount(String username);

    /**
     *
     * change the account of the provided username's password from oldPassword to newPassword
     *
     * @param username
     * @param oldPassword
     * @param newPassword
     */
    void changePassword(String username, String oldPassword, String newPassword) throws OldPasswordVerificationFailedException;

    /**
     * check if an account with username exists
     * @param username
     * @return
     */
    boolean accountExists(String username);

}
