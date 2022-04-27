package com.zantabri.auth_service.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zantabri.auth_service.AccountDetailsBuilder;
import com.zantabri.auth_service.model.AccountDetails;
import com.zantabri.auth_service.model.UserRole;
import com.zantabri.auth_service.repositories.AccountDetailsRepository;
import com.zantabri.auth_service.services.AccountDetailsService;
import com.zantabri.auth_service.services.AccountOnBoardingService;
import org.junit.jupiter.api.Test;
import org.mockito.internal.verification.NoInteractions;
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
    public void check() {
        assertNotNull(accountDetailsService);
    }

    @Test
    public void testRegister() throws Exception {

        var payload = "{\"username\":\"johnD\",\"email\":\"john@email.com\",\"password\":\"password\",\"telephone\":\"08055932559\",\"firstName\":\"John\",\"lastName\":\"doe\",\"activated\":false,\"authorities\":[{\"id\":1,\"role\":\"ADMIN\",\"authority\":\"ADMIN\"}],\"organizationId\":1}";
        mockMvc.perform(post("/register").contentType(MediaType.APPLICATION_JSON).content(payload))
                .andExpect(status().isOk()).andDo(result -> logger.info("message {}", result.getResponse().getContentAsString()));

        verify(accountOnBoardingService).register(any(AccountDetails.class));

    }

    @Test
    public void testRegisterWithInvalidAccountDetails() throws Exception {
        AccountDetails accountDetails = AccountDetailsBuilder.from( "johnD",
                "John",
                "doe",
                List.of(new UserRole(1,"ADMIN")),
                "password",
                "john@email.com",
                false,
                "08055932559",
                1);

        var payload = mapper.writeValueAsString(accountDetails);

        mockMvc.perform(post("/register").contentType(MediaType.APPLICATION_JSON).content(payload))
                .andExpect(status().is4xxClientError())
                .andDo(result -> logger.info("message {}", result.getResponse().getContentAsString()));

        verify(accountOnBoardingService, new NoInteractions()).register(any(AccountDetails.class));
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
