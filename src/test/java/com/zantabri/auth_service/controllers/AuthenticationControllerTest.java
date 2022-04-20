package com.zantabri.auth_service.controllers;

import com.zantabri.auth_service.repositories.AccountDetailsRepository;
import com.zantabri.auth_service.services.AccountDetailsService;
import com.zantabri.auth_service.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AuthenticationController.class)
public class AuthenticationControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AuthenticationService authenticationService;

    @MockBean
    AccountDetailsService accountDetailsService;

    @MockBean
    AccountDetailsRepository accountDetailsRepository;



}
