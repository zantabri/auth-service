package com.deepbluec.auth_service.services;

import com.deepbluec.auth_service.model.AccountDetails;
import org.springframework.data.domain.Page;

public interface AccountDetailsService {

    AccountDetails getAccountByUsername(String username);

    AccountDetails createAccount(AccountDetails accountDetails);

    AccountDetails updateAccount(String username, AccountDetails accountDetails);

    void deleteAccount(String username);

    void changePassword(String username, String oldPassword, String newPassword);

    boolean accountExists(String username);

    Page<AccountDetails> getAccountDetailsListPage(int page, int count, String sortDir, String sortBy);
}
