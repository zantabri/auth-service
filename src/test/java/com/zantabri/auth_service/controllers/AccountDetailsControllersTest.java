package com.zantabri.auth_service.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zantabri.auth_service.AccountDetailsBuilder;
import com.zantabri.auth_service.errors.ResourceNotFoundException;
import com.zantabri.auth_service.model.AccountDetails;
import com.zantabri.auth_service.model.UserRole;
import com.zantabri.auth_service.repositories.AccountDetailsRepository;
import com.zantabri.auth_service.services.AccountDetailsService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

@WebMvcTest(AccountDetailsController.class)
@TestPropertySource("classpath:application.properties")
public class AccountDetailsControllersTest {

    @Value("${jwt.signing.key}")
    private  String signingKey;

    private Logger logger = LoggerFactory.getLogger(AccountDetailsControllersTest.class);
    private String username = "johnD";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    private AccountDetailsService accountDetailsService;

    @MockBean
    private AccountDetailsRepository accountDetailsRepository;

    private static String jwt;

    @BeforeEach
    private void initJws() {
        SecretKey key = Keys.hmacShaKeyFor(signingKey.getBytes(StandardCharsets.UTF_8));
        jwt = Jwts.builder().setClaims(Map.of("username", username,"roles", "ROLE_ADMIN"))
                .signWith(key)
                .setExpiration(Date.from(LocalDateTime.now().plus(1, ChronoUnit.HOURS).atZone(ZoneId.systemDefault()).toInstant()))
                .compact();
    }

    @Test
    public void check() {
        assertNotNull(accountDetailsService);
    }

    @Test
    public void testGetAccountByUsername() throws Exception {

        AccountDetails accountDetails = AccountDetailsBuilder.from( "johnD",
                "John",
                "doe",
                List.of(new UserRole(1,"ADMIN")),
                null,
                "john@email.com",
                true,
                "08055932559",
                1);

        given(accountDetailsService.getAccountByUsername(anyString())).willReturn(accountDetails);

        mockMvc.perform(get("/accounts/".concat(username)).header("Authorization", jwt).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andDo(result1 -> {
                    logger.info("result is {}",result1.getResponse().getContentAsString());
                })
                .andExpect(jsonPath("$.password").doesNotExist())
                .andExpect(jsonPath("$.username").value("johnD"));

    }

    @Test
    public void testGetAccountByUsernameWhereUserNotFound() throws Exception {
        given(accountDetailsService.getAccountByUsername(anyString())).willThrow(new ResourceNotFoundException("username", AccountDetails.class));
        mockMvc.perform(get("/accounts/".concat(username)).header("Authorization", jwt).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()).andDo(result1 -> {
                    logger.info("result is {}",result1.getResponse().getContentAsString());
                });
    }

    @Test
    public void testUpdateAccount() throws Exception {

        AccountDetails update = AccountDetailsBuilder.from( "johnD",
                "John",
                "doe",
                List.of(new UserRole(1,"ADMIN")),
                null,
                "johnD@email.com",
                true,
                "08055932559",
                1);

        String sUpdate = mapper.writeValueAsString(update);
        mockMvc.perform(put("/accounts/".concat(username)).header("Authorization",jwt).contentType(MediaType.APPLICATION_JSON).content(sUpdate))
                .andExpect(status().isOk())
                .andDo(result -> logger.info("response {}", result.getResponse().getContentAsString()));

        verify(accountDetailsService).updateAccount(eq(username), any(AccountDetails.class));

    }

    @Test
    public void testUpdateAccountWhenAccountDoesntExist() throws Exception {

        AccountDetails update = AccountDetailsBuilder.from( "johnD2",
                "John",
                "doe",
                List.of(new UserRole(1,"ADMIN")),
                null,
                "johnD@email.com",
                true,
                "08055932559",
                1);

        String sUpdate = mapper.writeValueAsString(update);
        when(accountDetailsService.updateAccount(eq(username), any(AccountDetails.class))).thenThrow(new ResourceNotFoundException(1, AccountDetails.class));
        mockMvc.perform(put("/accounts/".concat(username)).header("Authorization",jwt).contentType(MediaType.APPLICATION_JSON).content(sUpdate))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateAccountWithMissingField() throws Exception {
        AccountDetails update = AccountDetailsBuilder.from( null,
                "John",
                null,
                List.of(new UserRole(1,"ADMIN")),
                null,
                "johnD@email.com",
                true,
                "08055932559",
                1);

        String sUpdate = mapper.writeValueAsString(update);
        when(accountDetailsService.updateAccount(eq(username), any(AccountDetails.class))).thenThrow(new ResourceNotFoundException(1, AccountDetails.class));
        mockMvc.perform(put("/accounts/".concat(username)).header("Authorization",jwt).contentType(MediaType.APPLICATION_JSON).content(sUpdate))
                .andDo(result -> logger.info("response is {}", result.getResponse().getContentAsString()))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testGetAccountsDetailsListPage() throws Exception {

        AccountDetails account1 = AccountDetailsBuilder.from( "johnD",
                "John",
                "doe",
                List.of(new UserRole(1,"ADMIN")),
                null,
                "johnD@email.com",
                true,
                "08055932559",
                1);

        AccountDetails account2 = AccountDetailsBuilder.from( "janeD",
                "Jane",
                "doe",
                List.of(new UserRole(1,"USER")),
                null,
                "janeD@email.com",
                true,
                "08064932360",
                1);


        given(accountDetailsService.getAccountDetailsListPage(anyInt(), anyInt(), anyString(), anyString())).willReturn(new PageImpl<>(List.of(account1, account2)));
        mockMvc.perform(get("/accounts").header("Authorization",jwt).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(result1 -> logger.info("result is {}",result1.getResponse().getContentAsString()))
                .andExpect(jsonPath("$.content[0].username").value("johnD"))
                .andExpect(jsonPath("$.content[1].username").value("janeD"));

    }

}
