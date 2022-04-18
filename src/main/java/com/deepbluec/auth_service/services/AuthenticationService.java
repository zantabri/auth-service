package com.deepbluec.auth_service.services;

import com.deepbluec.auth_service.security.AuthenticationCredentialsDTO;
import com.deepbluec.auth_service.security.AuthenticationResultDTO;

/**
 * Services authentication requests
 */
public interface AuthenticationService {

    AuthenticationResultDTO authenticate(AuthenticationCredentialsDTO credentials);

}
