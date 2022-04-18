package com.deepbluec.auth_service.services;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.deepbluec.auth_service.repositories.AccountDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;


@SpringBootTest
public class AccountDetailsServiceTest {

    @Autowired
    AccountDetailsService accountDetailsService;

    @MockBean
    AccountDetailsRepository accountDetailsRepository;

    @MockBean
    PasswordEncoder passwordEncoder;

    @Test
    public void check() {
        assertNotNull(accountDetailsService);
    }




}
