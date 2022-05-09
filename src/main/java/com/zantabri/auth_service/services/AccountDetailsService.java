package com.zantabri.auth_service.services;

import com.zantabri.auth_service.model.AccountDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface AccountDetailsService extends UserDetailsService {

    AccountDetails getAccountByUsername(String username);

    AccountDetails createAccount(AccountDetails accountDetails);

    AccountDetails updateAccount(String username, AccountDetails accountDetails);

    void deleteAccount(String username);

    void changePassword(String username, String oldPassword, String newPassword);

    boolean accountExists(String username);

    List<AccountDetails> getAccountDetailsListPage(int page, int count, String sortDir, String sortBy);


}
