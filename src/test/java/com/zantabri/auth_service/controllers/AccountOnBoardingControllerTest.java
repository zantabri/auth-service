package com.zantabri.auth_service.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.zantabri.auth_service.AccountDetailsBuilder;
import com.zantabri.auth_service.model.AccountDetails;
import com.zantabri.auth_service.model.UserRole;
import com.zantabri.auth_service.repositories.AccountDetailsRepository;
import com.zantabri.auth_service.services.AccountDetailsService;
import com.zantabri.auth_service.services.AccountOnBoardingService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

@WebMvcTest(AccountOnBoardingController.class)
@TestPropertySource("classpath:application.properties")
public class AccountOnBoardingControllerTest {

    private Logger logger = LoggerFactory.getLogger(AccountDetailsControllersTest.class);

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private AccountOnBoardingService accountOnBoardingService;

    @MockBean
    private AccountDetailsRepository accountDetailsRepository;

    @MockBean
    private AccountDetailsService accountDetailsService;

    @Autowired
    ObjectMapper mapper;

    @Test
    public void testRegister() throws Exception {

        AccountDetails accountDetails = AccountDetailsBuilder.from( "johnD",
                "John",
                "doe",
                List.of(new UserRole(1,"ADMIN")),
                "john@email.com",
                false,
                "08055932559",
                1);


        mockMvc.perform(post("/register").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(accountDetails)))
                .andExpect(status().isOk()).andDo(result -> logger.info("message {}", result.getResponse().getContentAsString()));

        verify(accountOnBoardingService).register(any(AccountDetails.class));

    }

    @Test
    public void testRegisterWithInvalidAccountDetails() {
        fail("validation and binding not yet implemented");
    }

    @Test
    public void testActivate() throws Exception {

        given(accountOnBoardingService.activate(eq("johnD"), eq("123456"))).willReturn(true);

        mockMvc.perform(get("/activate").param("urn","johnD").param("avc","123456"))
                .andExpect(status().isOk())
                .andExpect(view().name("onboarding/success"));

    }

    @Test
    public void testActivateWhereActivationFailed() throws Exception {

        given(accountOnBoardingService.activate(eq("johnD"), eq("123456"))).willReturn(false);

        mockMvc.perform(get("/activate").param("urn","johnD").param("avc","123456"))
                .andExpect(status().isOk())
                .andExpect(view().name("onboarding/failed"));
    }

}
