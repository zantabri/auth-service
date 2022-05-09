package com.zantabri.auth_service.security;

import aj.org.objectweb.asm.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zantabri.auth_service.errors.UnAuthorizedAccessException;
import com.zantabri.auth_service.model.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.chrono.ChronoZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    @Value("${jwt.signing.key}")
    private String signingKey;

    @Value("${jwt.expiration.zoneid}")
    private String zoneId;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String jwt = request.getHeader("Authorization");

        if (jwt == null || jwt.isEmpty())
            jwt = request.getHeader("authorization");

        if (jwt == null || jwt.isEmpty())
            throw new UnAuthorizedAccessException();

        SecretKey key = Keys.hmacShaKeyFor(signingKey.getBytes(StandardCharsets.UTF_8));
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jwt)
                .getBody();

        LocalDateTime expirationDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(claims.getExpiration().getTime()), ZoneId.of(zoneId));
        boolean credentialsNonExpired = LocalDateTime.now().atZone(ZoneId.of(zoneId)).isBefore(expirationDateTime.atZone(ZoneId.of(zoneId)));

        if (credentialsNonExpired == false) {
            throw new CredentialsExpiredException("Expired Credentials");
        }

        String username = String.valueOf(claims.get("username"));
        long organizationId = Long.valueOf(String.valueOf(claims.get("organizationId")));
        boolean accountNonExpired = Boolean.valueOf((Boolean)claims.get("accountNonExpired"));
        boolean accountNonLocked = Boolean.valueOf((Boolean)claims.get("accountNonLocked"));
        boolean enabled = Boolean.valueOf((Boolean) claims.get("enabled"));

        String roles = String.valueOf(claims.get("roles"));
        String[] arr = roles.split(",");
        List<UserRole> userRoles = Arrays.stream(arr).map(s -> new UserRole(s)).collect(Collectors.toList());

        var auth = new UsernamePasswordAuthenticationToken(new JWTUserDetails(username, organizationId, accountNonExpired, accountNonLocked, credentialsNonExpired, enabled, userRoles) , null, userRoles);
        SecurityContextHolder.getContext().setAuthentication(auth);

        filterChain.doFilter(request, response);

    }

    protected boolean shouldNotFilter(HttpServletRequest request) {

        String path = request.getPathInfo();
        if(path == null)
            path = request.getServletPath();

        return path.equals("/authenticate") || path.equals("/register") || path.equals("/activate") || path.equals("/ping");
    }
}
