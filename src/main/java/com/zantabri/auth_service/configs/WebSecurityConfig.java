package com.zantabri.auth_service.configs;

import com.zantabri.auth_service.repositories.AccountDetailsRepository;
import com.zantabri.auth_service.security.AuthenticationProviderImpl;
import com.zantabri.auth_service.security.UserDetailsManagerImpl;
import com.zantabri.auth_service.security.JWTAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JWTAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private AccountDetailsRepository accountDetailsRepository;

 /*   @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(userDetailsService())
                .passwordEncoder(passwordEncoder());

        auth.authenticationProvider(authenticationProvider());

    }*/


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable();
        http.addFilterAt(jwtAuthenticationFilter, BasicAuthenticationFilter.class);

        http.authorizeRequests()
                .mvcMatchers(HttpMethod.GET,"/ptsp/{id}").hasAnyRole("SUPER_ADMIN", "ADMIN")
                .mvcMatchers("/ptsp").hasRole("SUPER_ADMIN")
                .mvcMatchers("/register").permitAll()
                .mvcMatchers("/authenticate").permitAll()
                .mvcMatchers("/activate").permitAll()
                .mvcMatchers("/ping").permitAll()
                .anyRequest().authenticated();

    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        return new AuthenticationProviderImpl(passwordEncoder(), userDetailsService());
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsManagerImpl(accountDetailsRepository, passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(4);
    }

}
