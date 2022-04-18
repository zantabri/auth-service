package com.zantabri.auth_service.controllers;

import com.zantabri.auth_service.security.AuthenticationCredentialsDTO;
import com.zantabri.auth_service.security.AuthenticationResultDTO;
import com.zantabri.auth_service.services.AuthenticationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {


    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/authenticate")
    public AuthenticationResultDTO authenticate(@RequestBody AuthenticationCredentialsDTO credentials) {
        return authenticationService.authenticate(credentials);
    }

}
