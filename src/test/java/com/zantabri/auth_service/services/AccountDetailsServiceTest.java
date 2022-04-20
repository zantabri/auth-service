package com.zantabri.auth_service.services;

import static org.mockito.BDDMockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.zantabri.auth_service.errors.OldPasswordVerificationFailedException;
import com.zantabri.auth_service.errors.ResourceNotFoundException;
import com.zantabri.auth_service.model.AccountDetails;

import com.zantabri.auth_service.model.UserRole;
import org.junit.jupiter.api.Test;

import com.zantabri.auth_service.repositories.AccountDetailsRepository;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AccountDetailsServiceImpl.class, AccountDetailsRepository.class, PasswordEncoder.class})
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
        //TODO implement validation logic
        fail("not yet implemented");
    }

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
                null,
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

    @Test
    public void testUpdateAccountDetailsWhereAccountDetailsNotFound() {

        String username = "johnD";
        AccountDetails accountDetailsModification = accountDetails(
                null,
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

    @Test
    public void testDeleteAccount() {
        String username = "JohnD";
        accountDetailsService.deleteAccount(username);
        verify(accountDetailsRepository).deleteById(username);
    }


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

    @Test
    public void testChangePasswordWhenUserNotFound() {

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

        given(accountDetailsRepository.findById(eq(username))).willReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> accountDetailsService.changePassword(username, oldPassword, newPassword));

    }

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

    @Test
    public void testAccountExists() {
        String username = "johnD";
        accountDetailsService.accountExists(username);
        verify(accountDetailsRepository).existsById(username);

    }

    @Test
    public void testGetAccountDetailsListPage() {

        accountDetailsService.getAccountDetailsListPage(1, 1, "asc", "username");
        verify(accountDetailsRepository).findAll(any(Pageable.class));

    }



}
