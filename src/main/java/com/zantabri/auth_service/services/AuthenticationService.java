package com.zantabri.auth_service.services;

import com.zantabri.auth_service.security.AuthenticationCredentialsDTO;
import com.zantabri.auth_service.security.AuthenticationResultDTO;

/**
 * Services authentication requests
 */
public interface AuthenticationService {

    AuthenticationResultDTO authenticate(AuthenticationCredentialsDTO credentials);

}
