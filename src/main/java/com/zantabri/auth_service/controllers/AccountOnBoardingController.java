package com.zantabri.auth_service.controllers;

import com.zantabri.auth_service.model.AccountDetails;
import com.zantabri.auth_service.services.AccountOnBoardingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody void register(@RequestBody AccountDetails accountDetails) {
        this.accountOnBoardingService.register(accountDetails);
    }

    @GetMapping("/activate")
    public String activate(@RequestParam("urn") String username, @RequestParam("avc") String activationCode) {

        var success = this.accountOnBoardingService.activate(username, activationCode);

        if (success) {
            return "onboarding/success";
        } else {
            return "onboarding/failed";
        }
    }

}
