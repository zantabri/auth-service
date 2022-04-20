package com.zantabri.auth_service.services;

import static org.mockito.BDDMockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.zantabri.auth_service.AccountDetailsBuilder;
import com.zantabri.auth_service.model.AccountDetails;
import com.zantabri.auth_service.model.PTSPOrganization;
import com.zantabri.auth_service.model.UserRole;
import com.zantabri.auth_service.repositories.AccountDetailsRepository;
import com.zantabri.auth_service.security.AuthenticationCredentialsDTO;
import com.zantabri.auth_service.security.JWTAuthenticationResultDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {JWTAuthenticationServiceImpl.class, PasswordEncoder.class, AccountDetailsRepository.class})
@TestPropertySource(locations = "classpath:application.properties")
public class JWTAuthenticationServiceTest {

    @Autowired
    private AuthenticationService authenticationService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private AccountDetailsRepository accountDetailsRepository;

    @Test
    public void check() {
        assertNotNull(authenticationService);
    }

    @Test
    public void testAuthenticate() {

        AuthenticationCredentialsDTO credentialsDTO = new AuthenticationCredentialsDTO("johnD", "password");
        AccountDetails accountDetails = AccountDetailsBuilder.from( "johnD",
                "John",
                "doe",
                List.of(new UserRole(1,"Admin")),
                "john@email.com",
                true,
                "08064932359",
                1);

        accountDetails.setPassword("password");

        given(accountDetailsRepository.findById(credentialsDTO.getUsername())).willReturn(Optional.of(accountDetails));

        given(passwordEncoder.matches(eq(credentialsDTO.getPassword()), anyString())).willReturn(true);

        JWTAuthenticationResultDTO resultDTO = (JWTAuthenticationResultDTO) authenticationService.authenticate(credentialsDTO);

        assertNotNull(resultDTO);
        System.out.println("jwt is " + resultDTO.getToken());

    }

    @Test
    public void testAuthenticateWhereUserNotFound() {

        AuthenticationCredentialsDTO credentialsDTO = new AuthenticationCredentialsDTO("johnD", "password");

        given(accountDetailsRepository.findById(credentialsDTO.getUsername())).willReturn(Optional.empty());

        assertThrows(BadCredentialsException.class, () -> authenticationService.authenticate(credentialsDTO));

    }

    @Test
    public void testAuthenticateWherePasswordDoesntMatch() {

        AuthenticationCredentialsDTO credentialsDTO = new AuthenticationCredentialsDTO("johnD", "password");
        AccountDetails accountDetails = AccountDetailsBuilder.from( "johnD",
                "John",
                "doe",
                List.of(new UserRole(1,"Admin")),
                "john@email.com",
                true,
                "08064932359",
                1);

        accountDetails.setPassword("password");

        given(accountDetailsRepository.findById(credentialsDTO.getUsername())).willReturn(Optional.of(accountDetails));

        given(passwordEncoder.matches(eq(credentialsDTO.getPassword()), anyString())).willReturn(false);
        assertThrows(BadCredentialsException.class, () -> authenticationService.authenticate(credentialsDTO));

    }


}
