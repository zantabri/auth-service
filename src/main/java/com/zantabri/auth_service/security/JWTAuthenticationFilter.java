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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    @Value("${jwt.signing.key}")
    private String signingKey;

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

        String username = String.valueOf(claims.get("username"));
        String roles = String.valueOf(claims.get("roles"));
        String[] arr = roles.split(",");
        List<SimpleGrantedAuthority> authorities = Arrays.stream(arr).map(s -> new SimpleGrantedAuthority(s)).collect(Collectors.toList());
        var auth = new UsernamePasswordAuthenticationToken(username, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(auth);

        filterChain.doFilter(request, response);

    }

    protected boolean shouldNotFilter(HttpServletRequest request) {

        return request.getPathInfo().equals("/authenticate") || request.getPathInfo().equals("/register") || request.getPathInfo().equals("/activate") || request.getPathInfo().equals("/ping");
    }
}
