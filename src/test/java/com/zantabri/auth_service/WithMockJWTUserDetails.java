package com.zantabri.auth_service;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockJWTUserDetailsSecurityContextFactory.class)
public @interface WithMockJWTUserDetails {

    String username() default "johnD";
    long organizationId() default  1L;
    boolean accountNonExpired() default  true;
    boolean accountNonLocked() default true;
    boolean credentialsNonExpired() default true;
    boolean enabled() default true;
    String[] authorities() default {"ROLE_USER"};


}
