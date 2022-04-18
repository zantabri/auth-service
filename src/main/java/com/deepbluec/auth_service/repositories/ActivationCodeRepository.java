package com.deepbluec.auth_service.repositories;

import com.deepbluec.auth_service.model.ActivationCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ActivationCodeRepository extends JpaRepository<ActivationCode, String> {

    Optional<ActivationCode> findByUsernameAndCodeAndExpiresLessThan(String username, String code, LocalDateTime localDateTime);

}
