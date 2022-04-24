package com.zantabri.auth_service.services;

import com.zantabri.auth_service.errors.ResourcePayloadValidationException;
import com.zantabri.auth_service.errors.ResourceValidationException;
import com.zantabri.auth_service.repositories.AccountDetailsRepository;
import com.zantabri.auth_service.errors.OldPasswordVerificationFailedException;
import com.zantabri.auth_service.errors.ResourceNotFoundException;
import com.zantabri.auth_service.model.AccountDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Optional;
import java.util.Set;

@Transactional
@Service
public class AccountDetailsServiceImpl implements AccountDetailsService {

    private final AccountDetailsRepository accountDetailsRepository;
    private final PasswordEncoder passwordEncoder;
    private final Validator validator;


    @Autowired
    public AccountDetailsServiceImpl(AccountDetailsRepository accountDetailsRepository, PasswordEncoder passwordEncoder, Validator validator) {
        this.accountDetailsRepository = accountDetailsRepository;
        this.passwordEncoder = passwordEncoder;
        this.validator = validator;
    }

    @Override
    public AccountDetails getAccountByUsername(String username) {

        Optional<AccountDetails> optionalAccountDetails = accountDetailsRepository.findById(username);

        if (optionalAccountDetails.isEmpty()) {
            throw new ResourceNotFoundException(username, AccountDetails.class);
        }

        return optionalAccountDetails.get();

    }

    @Override
    public AccountDetails createAccount(AccountDetails accountDetails) {

        Set<ConstraintViolation<AccountDetails>> constraintViolationSet = validator.validate(accountDetails);
        if(!constraintViolationSet.isEmpty()) {
            throw new ResourceValidationException(constraintViolationSet.toString());
        }

        return accountDetailsRepository.save(accountDetails);

    }

    @Override
    public AccountDetails updateAccount(String username, AccountDetails accountDetails) {

        Set<ConstraintViolation<AccountDetails>> constraintViolationSet = validator.validate(accountDetails);

        if(!constraintViolationSet.isEmpty()) {
            throw new ResourceValidationException(constraintViolationSet.toString());
        }

        Optional<AccountDetails> optionalAccountDetails = accountDetailsRepository.findById(username);

        if (optionalAccountDetails.isEmpty()) {
            throw new ResourceNotFoundException(username, AccountDetails.class);
        }

        accountDetails.setUsername(username);

        return accountDetailsRepository.save(accountDetails);

    }

    @Override
    public void deleteAccount(String username) {
        accountDetailsRepository.deleteById(username);
    }

    @Override
    public void changePassword(String username, String oldPassword, String newPassword) {

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

    @Override
    public Page<AccountDetails> getAccountDetailsListPage( int page,int count, String sortDir, String sortBy) {

        Pageable pageable = PageRequest.of(page, count, Sort.by(Sort.Direction.fromString(sortDir), sortBy));
        return accountDetailsRepository.findAll(pageable);

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return (UserDetails) this.getAccountByUsername(username);
    }
}
