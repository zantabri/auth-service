package com.zantabri.auth_service.controllers;

import com.zantabri.auth_service.model.AccountDetails;
import com.zantabri.auth_service.services.AccountDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

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
    public void updateAccount(@PathVariable String username, @RequestBody AccountDetails accountDetails) {
        accountDetailsService.updateAccount(username, accountDetails);
    }

    @GetMapping
    public Page<AccountDetails> getAccountDetailsListPage(@RequestParam(defaultValue = "1", required = false) int page, @RequestParam(defaultValue = "10", required = false) int count, @RequestParam(defaultValue = "asc", required = false) String sortDir, @RequestParam(defaultValue = "username", required = false) String sortBy) {

        return accountDetailsService.getAccountDetailsListPage(page, count, sortDir, sortBy);

    }



}
