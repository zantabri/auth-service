package com.zantabri.auth_service.security;

import com.zantabri.auth_service.repositories.AccountDetailsRepository;
import com.zantabri.auth_service.errors.OldPasswordVerificationFailedException;
import com.zantabri.auth_service.model.AccountDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

public class UserDetailsManagerImpl implements UserDetailsManager {

    private final AccountDetailsRepository accountDetailsRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserDetailsManagerImpl(AccountDetailsRepository accountDetailsRepository, PasswordEncoder passwordEncoder) {
        this.accountDetailsRepository  = accountDetailsRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<AccountDetails> optionalAccountDetails = accountDetailsRepository.findById(username);
        if (optionalAccountDetails.isEmpty()) {
            throw new UsernameNotFoundException("unable to locate user with username '".concat(username).concat("' in the database"));
        }

        return new UserDetailsImpl(optionalAccountDetails.get());

    }

    @Override
    public AccountDetails createAccount(AccountDetails accountDetails) {

        return accountDetailsRepository.save(accountDetails);

    }

    @Override
    public AccountDetails updateAccount(AccountDetails accountDetails) {

        return accountDetailsRepository.save(accountDetails);

    }

    @Override
    public void deleteAccount(String username) {
        accountDetailsRepository.deleteById(username);
    }

    @Override
    public void changePassword(String username, String oldPassword, String newPassword) throws OldPasswordVerificationFailedException {
        Optional<AccountDetails> accountDetailsOpt = accountDetailsRepository.findById(username);

        if (accountDetailsOpt.isEmpty()) {
            throw new UsernameNotFoundException("Unable to locate account with username '".concat(username).concat("'"));
        }

        AccountDetails accountDetails = accountDetailsOpt.get();
        if (!passwordEncoder.matches(oldPassword, accountDetails.getPassword())) {
            throw new OldPasswordVerificationFailedException("old password verification failed");
        }

        accountDetails.setPassword(passwordEncoder.encode(newPassword));
        accountDetailsRepository.save(accountDetails);

    }

    @Override
    public boolean accountExists(String username) {
        return accountDetailsRepository.existsById(username);
    }

}
