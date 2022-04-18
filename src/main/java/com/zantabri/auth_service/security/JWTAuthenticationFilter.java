package com.zantabri.auth_service.security;

import com.zantabri.auth_service.errors.UnAuthorizedAccessException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
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
import java.util.List;

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
        List<GrantedAuthority> authorities = new ArrayList<>();

        String[] rolesArr = roles.split(",");
        for (int i = 0; i < rolesArr.length; i++) {

            if (rolesArr[i] != null && !rolesArr[i].isEmpty())
                authorities.add(new SimpleGrantedAuthority(rolesArr[i]));

        }
        var auth = new UsernamePasswordAuthenticationToken(username, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(auth);

        filterChain.doFilter(request, response);

    }

    protected boolean shouldNotFilter(HttpServletRequest request) {
        return request.getServletPath().equals("/authenticate") || request.getServletPath().equals("/register") || request.getServletPath().equals("/activate") || request.getServletPath().equals("/ping");
    }
}
