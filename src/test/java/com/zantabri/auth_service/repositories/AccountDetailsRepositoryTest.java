package com.zantabri.auth_service.repositories;

import static org.junit.jupiter.api.Assertions.*;

import com.zantabri.auth_service.AccountDetailsBuilder;
import com.zantabri.auth_service.model.AccountDetails;
import com.zantabri.auth_service.model.UserRole;
import com.zantabri.auth_service.security.UserDetailsImpl;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import javax.persistence.EntityManagerFactory;
import java.util.List;
import java.util.Optional;

@Sql(scripts = "classpath:db/init-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@DataJpaTest
public class AccountDetailsRepositoryTest {

    private final Logger logger = LoggerFactory.getLogger(AccountDetailsRepositoryTest.class);

    @Autowired
    private AccountDetailsRepository accountDetailsRepository;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void check() {
        assertNotNull(accountDetailsRepository);
    }

    @Test
    public void testFindByID() {

        Optional<AccountDetails> accountDetails = accountDetailsRepository.findById("dman2022");
        assertFalse(accountDetails.isEmpty());

        assertEquals("dapo",accountDetails.get().getFirstName());
        assertNotNull(accountDetailsRepository);
        assertNotNull(entityManagerFactory);

    }

    @Test
    public void testCreateAccount() {

        AccountDetails accountDetails = AccountDetailsBuilder.from( "johnD",
                "John",
                "doe",
                List.of(new UserRole(1,"ADMIN")),
                "password",
                "john@email.com",
                false,
                "08055932559",
                1);

        AccountDetails response = accountDetailsRepository.save(accountDetails);
        assertNotNull(response);
        logger.info("response is {}", response);

    }

    @Test
    public void testCreateAccountWithInValidFields() {

        AccountDetails accountDetails = AccountDetailsBuilder.from( "johnD",
                "John",
                "doe",
                List.of(new UserRole(1,"ADMIN")),
                "pa",
                "john@email.com",
                false,
                "08055932559",
                1);

        AccountDetails response = accountDetailsRepository.save(accountDetails);
        assertNotNull(response);
        logger.info("response is {}", response);
//        assertThrows(Exception.class, () -> accountDetailsRepository.save(accountDetails));

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
