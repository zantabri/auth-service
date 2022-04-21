package com.zantabri.auth_service.repositories;

import com.zantabri.auth_service.model.ActivationCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ActivationCodeRepository extends JpaRepository<ActivationCode, String> {

    /**
     * Gets the activation code that matches the criteria
     * TODO: WE PROBABLY DON'T WANT TO ACCEPT THE DATE BUT USE THE CURRENT DATE BY DEFAULT
     * @param username
     * @param code
     * @param localDateTime
     * @return
     */
    Optional<ActivationCode> findByUsernameAndCodeAndExpiresGreaterThan(String username, String code, LocalDateTime localDateTime);

}
