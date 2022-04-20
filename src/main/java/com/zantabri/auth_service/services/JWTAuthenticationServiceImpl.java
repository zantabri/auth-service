package com.zantabri.auth_service.services;

import com.zantabri.auth_service.repositories.AccountDetailsRepository;
import com.zantabri.auth_service.model.AccountDetails;
import com.zantabri.auth_service.model.UserRole;
import com.zantabri.auth_service.security.AuthenticationCredentialsDTO;
import com.zantabri.auth_service.security.AuthenticationResultDTO;
import com.zantabri.auth_service.security.JWTAuthenticationResultDTO;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class JWTAuthenticationServiceImpl implements AuthenticationService {

    private final PasswordEncoder passwordEncoder;
    private final AccountDetailsRepository accountDetailsRepository;

    @Value("${jwt.signing.key}")
    private String signingKey;

    @Value("${jwt.token.valid.duration}")
    private int tokenDurationInHours;

    @Autowired
    public JWTAuthenticationServiceImpl(PasswordEncoder passwordEncoder, AccountDetailsRepository accountDetailsRepository) {
        this.passwordEncoder = passwordEncoder;
        this.accountDetailsRepository = accountDetailsRepository;
    }

    @Override
    public AuthenticationResultDTO authenticate(AuthenticationCredentialsDTO credentials) {

        Optional<AccountDetails> opt = accountDetailsRepository.findById(credentials.getUsername());
        if (opt.isEmpty()) {
            throw new BadCredentialsException("username not found");
        }

        if(!passwordEncoder.matches(credentials.getPassword(), opt.get().getPassword())) {
            throw new BadCredentialsException("credentials failed");
        }

        SecretKey key = Keys.hmacShaKeyFor(signingKey.getBytes(StandardCharsets.UTF_8));
        List<UserRole> userRoles = opt.get().getAuthorities();
        String userRolesString = commaSeparatedListOfRoles(userRoles);

        String jwt = Jwts.builder().setClaims(Map.of("username", credentials.getUsername(),"roles", userRolesString))
                .signWith(key)
                .setExpiration(Date.from(LocalDateTime.now().plus(tokenDurationInHours, ChronoUnit.HOURS).atZone(ZoneId.systemDefault()).toInstant()))
                .compact();

        return new JWTAuthenticationResultDTO(credentials.getUsername(), jwt);

    }

    private String commaSeparatedListOfRoles(List<UserRole> roles) {
        return roles.stream()
                .map(userRoles -> userRoles.getRole())
                .reduce((s, s2) -> s.concat(",").concat("ROLE_").concat(s2)).get();
    }

}
