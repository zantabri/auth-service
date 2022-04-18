package com.deepbluec.auth_service.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController {

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/ping")
    public String ping() {
        return "pong!";
    }

}
