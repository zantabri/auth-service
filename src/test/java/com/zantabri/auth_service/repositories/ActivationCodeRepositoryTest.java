package com.zantabri.auth_service.repositories;

import static org.junit.jupiter.api.Assertions.*;

import com.zantabri.auth_service.model.ActivationCode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.Optional;

@Sql(scripts = "classpath:db/init-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@DataJpaTest
public class ActivationCodeRepositoryTest {

    @Autowired
    ActivationCodeRepository activationCodeRepository;

    @Test
    public void check() {

        assertNotNull(activationCodeRepository);

    }


    @Test
    public void testFindByUsernameAndCodeAndExpiresGreaterThan() {

        Optional<ActivationCode> accessCode = activationCodeRepository.findByUsernameAndCodeAndExpiresGreaterThan("johnD", "154745", LocalDateTime.now());
        assertFalse(accessCode.isEmpty());
        System.out.println(accessCode.get());

    }

    @Test
    public void testFindByUsernameAndCodeAndExpiresGreaterThanWhereActivationCodeExpired() {
        Optional<ActivationCode> accessCode = activationCodeRepository.findByUsernameAndCodeAndExpiresGreaterThan("dman2022", "235621", LocalDateTime.now());
        assertTrue(accessCode.isEmpty());
    }

    @Test
    public void testFindByUsernameAndCodeAndExpiresGreaterThanWhereActivationCodeUsernameNotExits() {
        Optional<ActivationCode> accessCode = activationCodeRepository.findByUsernameAndCodeAndExpiresGreaterThan("dman20", "235621", LocalDateTime.now());
        assertTrue(accessCode.isEmpty());
    }

    @Test
    public void testFindByUsernameAndCodeAndExpiresGreaterThanWhereActivationCodeCodeNotExits() {
        Optional<ActivationCode> accessCode = activationCodeRepository.findByUsernameAndCodeAndExpiresGreaterThan("dman2022", "000000", LocalDateTime.now());
        assertTrue(accessCode.isEmpty());
    }
}
