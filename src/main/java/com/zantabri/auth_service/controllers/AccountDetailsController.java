package com.zantabri.auth_service.controllers;

import com.zantabri.auth_service.errors.ResourcePayloadValidationException;
import com.zantabri.auth_service.model.AccountDetails;
import com.zantabri.auth_service.services.AccountDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountDetailsController {

    private AccountDetailsService accountDetailsService;

    @Autowired
    public AccountDetailsController(AccountDetailsService accountDetailsService) {
        this.accountDetailsService = accountDetailsService;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public AccountDetails getAccountByUsername(@PathVariable String username) {
        return accountDetailsService.getAccountByUsername(username);
    }

    @PutMapping(value = "/{username}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void updateAccount(@PathVariable String username, @Validated(AccountDetails.AccountDetailsUpdatingValidation.class) @RequestBody AccountDetails accountDetails, BindingResult result) {
        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());
            throw new ResourcePayloadValidationException(accountDetails, result.getAllErrors().toString());
        }
        accountDetailsService.updateAccount(username, accountDetails);
    }

    @GetMapping
    public Page<AccountDetails> getAccountDetailsListPage(@RequestParam(defaultValue = "1", required = false) int page, @RequestParam(defaultValue = "10", required = false) int count, @RequestParam(defaultValue = "asc", required = false) String sortDir, @RequestParam(defaultValue = "username", required = false) String sortBy) {


        List<AccountDetails> accountDetails = accountDetailsService.getAccountDetailsListPage(page, count, sortDir, sortBy);
        Pageable pageable = PageRequest.of(page, count, Sort.by(Sort.Direction.fromString(sortDir), sortBy));
        return new PageImpl<>(accountDetails, pageable, null == accountDetails ? 0L : (long)accountDetails.size());

    }



}
