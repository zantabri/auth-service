package com.zantabri.auth_service.services;

import static org.mockito.BDDMockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.zantabri.auth_service.AccountDetailsBuilder;
import com.zantabri.auth_service.WithMockJWTUserDetails;
import com.zantabri.auth_service.WithMockJWTUserDetailsSecurityContextFactory;
import com.zantabri.auth_service.errors.OldPasswordVerificationFailedException;
import com.zantabri.auth_service.errors.ResourceNotFoundException;
import com.zantabri.auth_service.errors.ResourceValidationException;
import com.zantabri.auth_service.model.AccountDetails;

import com.zantabri.auth_service.model.UserRole;
import org.junit.jupiter.api.Test;

import com.zantabri.auth_service.repositories.AccountDetailsRepository;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.internal.verification.NoInteractions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.ConstraintViolation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@ExtendWith(SpringExtension.class)
@EnableMethodSecurity(prePostEnabled = true)
@ContextConfiguration(classes = {AccountDetailsServiceImpl.class, AccountDetailsRepository.class, PasswordEncoder.class, LocalValidatorFactoryBean.class, WithMockJWTUserDetailsSecurityContextFactory.class, WithSecurityContextTestExecutionListener.class})
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


    private AccountDetails accountDetails(String username, String firstName, String lastname, List<UserRole> userRoles,
                                          String email, boolean activated, String telephone, long organizationId) {

        AccountDetails accountDetails = new AccountDetails();
        accountDetails.setUsername(username);
        accountDetails.setActivated(activated);
        accountDetails.setAuthorities(userRoles);
        accountDetails.setEmail(email);
        accountDetails.setFirstName(firstName);
        accountDetails.setLastName(lastname);
        accountDetails.setOrganizationId(organizationId);
        accountDetails.setTelephone(telephone);

        return accountDetails;

    }

    @WithMockJWTUserDetails()
    @Test
    public void testPositiveGetAccountByUsername() {

        String username = "johnD";
        AccountDetails mockAccountDetails = accountDetails(
                username,
                "John",
                "doe",
                List.of(new UserRole(1,"Admin")),
                "john@email.com",
                true,
                "08064932359",
                1);
        given(accountDetailsRepository.findById(eq(username))).willReturn(Optional.of(mockAccountDetails));

        AccountDetails response = accountDetailsService.getAccountByUsername(username);
        assertNotNull(response);
        assertEquals(mockAccountDetails, response);

    }

    @WithMockJWTUserDetails(username = "deeJ")
    @Test
    public void testGetAccountByUsernameWhereTheLoggedInUserIsDifferent() {

        String username = "johnD";
        AccountDetails mockAccountDetails = accountDetails(
                username,
                "John",
                "doe",
                List.of(new UserRole(1,"Admin")),
                "john@email.com",
                true,
                "08064932359",
                1);

        given(accountDetailsRepository.findById(eq(username))).willReturn(Optional.of(mockAccountDetails));

        assertThrows(AccessDeniedException.class, () -> accountDetailsService.getAccountByUsername(username));
        verify(accountDetailsRepository, new NoInteractions()).findById(eq(username));

    }

    @WithMockJWTUserDetails(username = "deeJ", authorities = {"ROLE_SUPER_ADMIN"})
    @Test
    public void testGetAccountWhereLoggedInUserIsSuperAdmin() {

        String username = "johnD";
        AccountDetails mockAccountDetails = accountDetails(
                username,
                "John",
                "doe",
                List.of(new UserRole(1,"Admin")),
                "john@email.com",
                true,
                "08064932359",
                1);

        given(accountDetailsRepository.findById(eq(username))).willReturn(Optional.of(mockAccountDetails));


        AccountDetails response = accountDetailsService.getAccountByUsername(username);
        assertNotNull(response);
        assertEquals(mockAccountDetails, response);

    }

    @WithMockJWTUserDetails(username = "deeJ", organizationId = 1, authorities = {"ROLE_ADMIN"})
    @Test
    public void testGetAccountByWhereLoggedInUserIsAdmin() {

        String username = "johnD";
        AccountDetails mockAccountDetails = accountDetails(
                username,
                "John",
                "doe",
                List.of(new UserRole(1,"Admin")),
                "john@email.com",
                true,
                "08064932359",
                1);

        given(accountDetailsRepository.findById(eq(username))).willReturn(Optional.of(mockAccountDetails));

        AccountDetails response = accountDetailsService.getAccountByUsername(username);
        assertNotNull(response);
        assertEquals(mockAccountDetails, response);

    }

    @WithMockJWTUserDetails(username = "deeJ", organizationId = 2L, authorities = {"ROLE_ADMIN"})
    @Test
    public void testGetAccountByWhereLoggedInUserIsAdminWithDifferentOrganization() {

        fail("not yet implemented, should only work where the user being updated has the same organizationId has the logged in user and the logged in user is an admin");

    }

    @WithMockJWTUserDetails()
    @Test
    public void testGetAccountByUsernameWhereAccountNotFound() {

        String username = "johnD";
        given(accountDetailsRepository.findById(eq(username))).willReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> accountDetailsService.getAccountByUsername(username));

    }

    @Test
    public void testCreateAccountWithCorrectParameters() {
        String username = "johnD";
        AccountDetails details = accountDetails(
                username,
                "John",
                "doe",
                List.of(new UserRole(1,"Admin")),
                "john@email.com",
                true,
                "08064932359",
                1);

        accountDetailsService.createAccount(details);
        verify(accountDetailsRepository).save(details);
    }


    @Test
    public void testCreateAccountWithMissingParameters() {

        ConstraintViolation violation = mock(ConstraintViolation.class);

        AccountDetails details = accountDetails(
                null,
                "John",
                null,
                List.of(new UserRole(1,"Admin")),
                "john@email.com",
                true,
                "08064932359",
                1);

        assertThrows(ResourceValidationException.class, () -> accountDetailsService.createAccount(details));
        verify(accountDetailsRepository, new NoInteractions()).save(details);


    }

    @WithMockJWTUserDetails()
    @Test
    public void testUpdateAccount() {

        String username = "johnD";
        AccountDetails currentAccountDetails = accountDetails(
                username,
                "John",
                "doe",
                List.of(new UserRole(1,"ADMIN")),
                "john@email.com",
                true,
                "08064932359",
                1);


        AccountDetails accountDetailsModification = accountDetails(
                username,
                "John",
                "doe",
                List.of(new UserRole(1,"USER")),
                "jaeger@email.com",
                true,
                "08064932359",
                1);


        given(accountDetailsRepository.findById(eq(username))).willReturn(Optional.of(currentAccountDetails));
        accountDetailsService.updateAccount(username, accountDetailsModification);
        assertEquals(username, accountDetailsModification.getUsername());
        verify(accountDetailsRepository).save(accountDetailsModification);

    }

    @WithMockJWTUserDetails(username = "deeJ")
    @Test
    public void testUpdateAccountWhereLoggedInUserHasDifferentUsername() {

        String username = "johnD";
        AccountDetails currentAccountDetails = accountDetails(
                username,
                "John",
                "doe",
                List.of(new UserRole(1,"ADMIN")),
                "john@email.com",
                true,
                "08064932359",
                1);


        AccountDetails accountDetailsModification = accountDetails(
                username,
                "John",
                "doe",
                List.of(new UserRole(1,"USER")),
                "jaeger@email.com",
                true,
                "08064932359",
                1);


        given(accountDetailsRepository.findById(eq(username))).willReturn(Optional.of(currentAccountDetails));
        assertThrows(AccessDeniedException.class, () -> accountDetailsService.updateAccount(username, accountDetailsModification));
        verify(accountDetailsRepository, new NoInteractions()).findById(eq(username));

    }

    @WithMockJWTUserDetails(username = "deeJ", authorities = {"ROLE_SUPER_ADMIN"})
    @Test
    public void testUpdateAccountWhereLoggedInUserIsSuperAdmin() {

        String username = "johnD";
        AccountDetails currentAccountDetails = accountDetails(
                username,
                "John",
                "doe",
                List.of(new UserRole(1,"ADMIN")),
                "john@email.com",
                true,
                "08064932359",
                1);


        AccountDetails accountDetailsModification = accountDetails(
                username,
                "John",
                "doe",
                List.of(new UserRole(1,"USER")),
                "jaeger@email.com",
                true,
                "08064932359",
                1);


        given(accountDetailsRepository.findById(eq(username))).willReturn(Optional.of(currentAccountDetails));
        accountDetailsService.updateAccount(username, accountDetailsModification);
        assertEquals(username, accountDetailsModification.getUsername());
        verify(accountDetailsRepository).save(accountDetailsModification);

    }

    @WithMockJWTUserDetails(username = "deeJ", organizationId = 1L, authorities = {"ROLE_ADMIN"})
    @Test
    public void testUpdateAccountWhereLoggedInUserIsAdmin() {

        String username = "johnD";
        AccountDetails currentAccountDetails = accountDetails(
                username,
                "John",
                "doe",
                List.of(new UserRole(1,"ADMIN")),
                "john@email.com",
                true,
                "08064932359",
                1);


        AccountDetails accountDetailsModification = accountDetails(
                username,
                "John",
                "doe",
                List.of(new UserRole(1,"USER")),
                "jaeger@email.com",
                true,
                "08064932359",
                1);


        given(accountDetailsRepository.findById(eq(username))).willReturn(Optional.of(currentAccountDetails));
        accountDetailsService.updateAccount(username, accountDetailsModification);
        assertEquals(username, accountDetailsModification.getUsername());
        verify(accountDetailsRepository).save(accountDetailsModification);

    }

    @WithMockJWTUserDetails()
    @Test
    public void testUpdateAccountWithInvalidField() {

        String username = "johnD";
        AccountDetails currentAccountDetails = accountDetails(
                username,
                "John",
                "doe",
                List.of(new UserRole(1,"ADMIN")),
                "john@email.com",
                true,
                "08064932359",
                1);


        AccountDetails accountDetailsModification = accountDetails(
                username,
                "John",
                "doe",
                List.of(new UserRole(1,"USER")),
                "jaeger",
                true,
                "08064932359",
                1);

        given(accountDetailsRepository.findById(eq(username))).willReturn(Optional.of(currentAccountDetails));

        assertThrows(ResourceValidationException.class, () -> accountDetailsService.updateAccount(username, accountDetailsModification));
        verify(accountDetailsRepository, new NoInteractions()).save(accountDetailsModification);

    }

    @WithMockJWTUserDetails()
    @Test
    public void testUpdateAccountDetailsWhereAccountDetailsNotFound() {

        String username = "johnD";
        AccountDetails accountDetailsModification = accountDetails(
                username,
                "John",
                "doe",
                List.of(new UserRole(1,"USER")),
                "jaeger@email.com",
                true,
                "08064932359",
                1);

        given(accountDetailsRepository.findById(eq(username))).willReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> accountDetailsService.updateAccount(username, accountDetailsModification));

    }

    @WithMockJWTUserDetails()
    @Test
    public void testDeleteAccount() {
        String username = "johnD";
        accountDetailsService.deleteAccount(username);
        verify(accountDetailsRepository).deleteById(username);
    }

    @WithMockJWTUserDetails(username = "deeJ")
    @Test
    public void testDeleteAccountWhereLoggedInUserHasDifferentUsername() {
        String username = "johnD";
        assertThrows(AccessDeniedException.class, () -> accountDetailsService.deleteAccount(username));
        verify(accountDetailsRepository, new NoInteractions()).deleteById(username);
    }


    @WithMockJWTUserDetails()
    @Test
    public void testChangePassword() {

        String username = "johnD";
        String oldPassword = "oldpassword";
        String newPassword = "newpassword";
        AccountDetails accountDetails = accountDetails(
                username,
                "John",
                "doe",
                List.of(new UserRole(1,"ADMIN")),
                "john@email.com",
                true,
                "08064932359",
                1);

        given(accountDetailsRepository.findById(eq(username))).willReturn(Optional.of(accountDetails));
        given(passwordEncoder.matches(eq(oldPassword), eq(accountDetails.getPassword()))).willReturn(true);

        accountDetailsService.changePassword(username, oldPassword, newPassword);
        verify(accountDetailsRepository).save(accountDetails);

    }

    @WithMockJWTUserDetails(username = "deeJ")
    @Test
    public void testChangePasswordWhereLoggedInUserHasDifferentUsername() {

        String username = "johnD";
        String oldPassword = "oldpassword";
        String newPassword = "newpassword";
        AccountDetails accountDetails = accountDetails(
                username,
                "John",
                "doe",
                List.of(new UserRole(1,"ADMIN")),
                "john@email.com",
                true,
                "08064932359",
                1);

        given(accountDetailsRepository.findById(eq(username))).willReturn(Optional.of(accountDetails));
        given(passwordEncoder.matches(eq(oldPassword), eq(accountDetails.getPassword()))).willReturn(true);

        assertThrows(AccessDeniedException.class,() -> accountDetailsService.changePassword(username, oldPassword, newPassword));
        verify(accountDetailsRepository, new NoInteractions()).save(accountDetails);

    }

    @WithMockJWTUserDetails
    @Test
    public void testChangePasswordWhenUserNotFound() {

        String username = "johnD";
        String oldPassword = "oldpassword";
        String newPassword = "newpassword";

        given(accountDetailsRepository.findById(eq(username))).willReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> accountDetailsService.changePassword(username, oldPassword, newPassword));

    }

    @WithMockJWTUserDetails
    @Test
    public void testChangePasswordWhenOldPasswordIsIncorrect() {

        String username = "johnD";
        String oldPassword = "oldpassword";
        String newPassword = "newpassword";
        AccountDetails accountDetails = accountDetails(
                username,
                "John",
                "doe",
                List.of(new UserRole(1,"ADMIN")),
                "john@email.com",
                true,
                "08064932359",
                1);

        given(accountDetailsRepository.findById(eq(username))).willReturn(Optional.of(accountDetails));
        given(passwordEncoder.matches(eq(oldPassword), eq(accountDetails.getPassword()))).willReturn(false);

        assertThrows(OldPasswordVerificationFailedException.class, () -> accountDetailsService.changePassword(username, oldPassword, newPassword));

    }

    @WithMockJWTUserDetails(username = "deeJ", authorities = {"ROLE_SUPER_ADMIN"})
    @Test
    public void testAccountExistsWhereLoggedInUserIsSuperAdmin() {
        String username = "johnD";
        accountDetailsService.accountExists(username);
        verify(accountDetailsRepository).existsById(username);
    }

    @WithMockJWTUserDetails(username = "deeJ", organizationId = 1L, authorities = {"ROLE_ADMIN"})
    @Test
    public void testAccountExistsWhereLoggedInUserIsAdmin() {
        fail("not yet implemented, should only work where the user being accessed has the same organizationId has the logged in user and the logged in user is an admin");
    }

    @WithMockJWTUserDetails
    @Test
    public void testAccountExistsWhereLoggedInUserIsUser() {
        String username = "johnD";
        assertThrows(AccessDeniedException.class, () -> accountDetailsService.accountExists(username));
        verify(accountDetailsRepository, new NoInteractions()).existsById(username);
    }



    @WithMockJWTUserDetails(username = "deeJ", authorities = {"ROLE_SUPER_ADMIN"})
    @Test
    public void testGetAccountDetailsListPageWhereLoggedInUserIsSuperAdmin() {

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
                2);

        ArrayList<AccountDetails> list = new ArrayList<>();
        list.add(account1);
        list.add(account2);

        given(accountDetailsRepository.secureFindAll(eq(1), eq(10), eq("asc"), eq("username"))).willReturn(list);
        List<AccountDetails> accountDetails = accountDetailsService.getAccountDetailsListPage(1, 10, "asc", "username");
        verify(accountDetailsRepository).secureFindAll(eq(1), eq(10), eq("asc"), eq("username"));
        assertEquals(2, accountDetails.size());

    }

    @WithMockJWTUserDetails
    @Test
    public void testGetAccountDetailsListPageWhereLoggedInUserIsUser() {

        assertThrows(AccessDeniedException.class, () -> accountDetailsService.getAccountDetailsListPage(1, 1, "asc", "username"));
        verify(accountDetailsRepository, new NoInteractions()).secureFindAll(anyInt(), anyInt(), anyString(), anyString());

    }

    @WithMockJWTUserDetails(username = "deeJ", organizationId = 1L, authorities = {"ROLE_ADMIN"})
    @Test
    public void testGetAccountDetailsListPageWhereLoggedInUserIsAdmin() {

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
                List.of(new UserRole(2,"USER")),
                null,
                "janeD@email.com",
                true,
                "08064932360",
                2);

        ArrayList<AccountDetails> list = new ArrayList<>();
        list.add(account1);
        list.add(account2);
        given(accountDetailsRepository.secureFindAll(eq(1), eq(10), eq("asc"), eq("username"))).willReturn(list);
        List<AccountDetails> accountDetails = accountDetailsService.getAccountDetailsListPage(1, 10, "asc", "username");
        verify(accountDetailsRepository).secureFindAll(eq(1), eq(10), eq("asc"), eq("username"));
        assertEquals(1, accountDetails.size());
        assertEquals(1, accountDetails.get(0).getOrganizationId());

    }



}
