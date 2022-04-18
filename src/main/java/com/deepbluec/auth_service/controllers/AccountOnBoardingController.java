package com.deepbluec.auth_service.controllers;

import com.deepbluec.auth_service.model.AccountDetails;
import com.deepbluec.auth_service.services.AccountOnBoardingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping
public class AccountOnBoardingController {

    private final AccountOnBoardingService accountOnBoardingService;

    @Autowired
    public AccountOnBoardingController(AccountOnBoardingService accountOnBoardingService) {
        this.accountOnBoardingService = accountOnBoardingService;
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/register")
    public @ResponseBody void register(@RequestBody AccountDetails accountDetails) {
        this.accountOnBoardingService.register(accountDetails);
    }

    @PostMapping("/activate")
    public String activate(@RequestParam("urn") String username, @RequestParam("avc") String activationCode) {

        var success = this.accountOnBoardingService.activate(username, activationCode);

        if (success) {
            return "onboarding/success";
        } else {
            return "onboarding/failed";
        }
    }

}
