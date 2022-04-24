package com.zantabri.auth_service.services;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.BDDMockito.*;
import com.zantabri.auth_service.AccountDetailsBuilder;
import com.zantabri.auth_service.errors.ResourceNotFoundException;
import com.zantabri.auth_service.errors.ResourceValidationException;
import com.zantabri.auth_service.model.AccountDetails;
import com.zantabri.auth_service.model.ActivationCode;
import com.zantabri.auth_service.model.UserRole;
import com.zantabri.auth_service.model.validators.ActivationCodeValidator;
import com.zantabri.auth_service.repositories.AccountDetailsRepository;
import com.zantabri.auth_service.repositories.ActivationCodeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.internal.verification.NoInteractions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AccountOnBoardingServiceImpl.class, AccountDetailsRepository.class, ActivationCodeRepository.class, NotificationService.class, LocalValidatorFactoryBean.class})
@TestPropertySource(locations = "classpath:application.properties")
public class AccountOnBoardingServiceTest {

    @Autowired
    AccountOnBoardingService accountOnBoardingService;

    @MockBean
    AccountDetailsRepository accountDetailsRepository;

    @MockBean
    ActivationCodeRepository activationCodeRepository;

    @MockBean
    NotificationService notificationService;



    @Test
    public void check() {
        assertNotNull(accountOnBoardingService);
    }

    @Test
    public void testRegister() {

        AccountDetails accountDetails = AccountDetailsBuilder.from( "johnD",
                "John",
                "doe",
                List.of(new UserRole(1,"Admin")),
                "password",
                "john@email.com",
                false,
                "08064932359",
                1);

        accountOnBoardingService.register(accountDetails);

        verify(notificationService).sendNotification(eq(accountDetails.getEmail()), eq("Activation Link"), anyString());
        verify(activationCodeRepository).save(any(ActivationCode.class));

    }

    @Test
    public void testRegisterWithInvalidParameters() {
        AccountDetails accountDetails = AccountDetailsBuilder.from( "johnD",
                "John",
                "doe",
                List.of(new UserRole(1,"Admin")),
                "pas",
                "john@email.com",
                false,
                "08064932359",
                1);


        assertThrows(ResourceValidationException.class, () -> accountOnBoardingService.register(accountDetails));

        verify(notificationService,new NoInteractions()).sendNotification(eq(accountDetails.getEmail()), eq("Activation Link"), anyString());
        verify(activationCodeRepository, new NoInteractions()).save(any(ActivationCode.class));
    }

    @Test
    public void testActivate() {

        String username = "johnD";
        String code = "4523650";

        AccountDetails accountDetails = AccountDetailsBuilder.from( "johnD",
                "John",
                "doe",
                List.of(new UserRole(1,"Admin")),
                null,
                "john@email.com",
                false,
                "08064932359",
                1);

        given(activationCodeRepository.findByUsernameAndCodeAndExpiresGreaterThan(eq(username), eq(code), any(LocalDateTime.class))).willReturn(Optional.of(new ActivationCode(username, code, LocalDateTime.now().plus(2, ChronoUnit.HOURS))));
        given(accountDetailsRepository.findById(eq(username))).willReturn(Optional.of(accountDetails));

        boolean success = accountOnBoardingService.activate(username, code);

        verify(accountDetailsRepository).save(accountDetails);
        assertTrue(accountDetails.isActivated());
        assertTrue(success);

    }

    @Test
    public void testActivateWhenCodeNotFound() {

        String username = "johnD";
        String code = "4523650";

        given(activationCodeRepository.findByUsernameAndCodeAndExpiresGreaterThan(eq(username), eq(code), any(LocalDateTime.class))).willReturn(Optional.empty());
        boolean success = accountOnBoardingService.activate(username, code);
        assertFalse(success);
    }

    @Test
    public void testActivateWhenUserNotFound() {

        String username = "johnD";
        String code = "4523650";

        given(activationCodeRepository.findByUsernameAndCodeAndExpiresGreaterThan(eq(username), eq(code), any(LocalDateTime.class))).willReturn(Optional.of(new ActivationCode(username, code, LocalDateTime.now().plus(2, ChronoUnit.HOURS))));
        given(accountDetailsRepository.findById(eq(username))).willReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> accountOnBoardingService.activate(username, code));

    }

}
