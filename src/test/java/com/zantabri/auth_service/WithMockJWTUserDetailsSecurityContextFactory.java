package com.zantabri.auth_service;

import com.zantabri.auth_service.model.UserRole;
import com.zantabri.auth_service.security.JWTUserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.stream.Collectors;

public class WithMockJWTUserDetailsSecurityContextFactory implements WithSecurityContextFactory<WithMockJWTUserDetails> {


    @Override
    public SecurityContext createSecurityContext(WithMockJWTUserDetails annotation) {

        SecurityContext security = SecurityContextHolder.createEmptyContext();

        JWTUserDetails userDetails = new JWTUserDetails(
                annotation.username(),
                annotation.organizationId(),
                annotation.accountNonExpired(),
                annotation.accountNonLocked(),
                annotation.credentialsNonExpired(),
                annotation.enabled(),
                Arrays.stream(annotation.authorities()).map(s -> new UserRole(s)).collect(Collectors.toList())
        );

        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        security.setAuthentication(auth);
        return security;
    }
}
