package com.zantabri.auth_service;

import static org.junit.jupiter.api.Assertions.*;

import com.zantabri.auth_service.model.AccountDetails;
import com.zantabri.auth_service.security.UserDetailsImpl;
import com.zantabri.auth_service.repositories.AccountDetailsRepository;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import javax.persistence.EntityManagerFactory;
import java.util.Optional;

@Sql(scripts = "classpath:db/init-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@DataJpaTest
public class AccountDetailsRepositoryTest {

    private final Logger logger = LoggerFactory.getLogger(AccountDetailsRepositoryTest.class);

    @Autowired
    private AccountDetailsRepository accountDetailsRepository;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Test
    public void testFindByID() {

        Optional<AccountDetails> accountDetails = accountDetailsRepository.findById("dman2022");
        assertFalse(accountDetails.isEmpty());

        assertEquals("dapo",accountDetails.get().getFirstName());
        assertNotNull(accountDetailsRepository);
        assertNotNull(entityManagerFactory);

    }

    @Test
    public void testCreateAuthenticationDetailsFromAccountDetails() {

        Optional<AccountDetails> accountDetails = accountDetailsRepository.findById("dman2022");
        assertFalse(accountDetails.isEmpty());
        UserDetailsImpl authDetails = new UserDetailsImpl(accountDetails.get());
        assertTrue(authDetails.isAccountNonExpired());
        logger.info("auth details is {}", authDetails);

    }

}
