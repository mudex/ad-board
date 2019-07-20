package com.shehab.adboard.zuulgatewayui.config.jwt;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        Claims claims = null;
        String authHeader = req.getHeader("Authorization");

        if (Objects.nonNull(authHeader) && authHeader.startsWith("Bearer ")) {
            claims = jwtUtil
                    .getClaims(authHeader.substring(7));
        }

        if (Objects.nonNull(claims) &&
                Objects.isNull(
                        SecurityContextHolder.getContext().getAuthentication()
                )) {
            String username = claims.getSubject();
            @SuppressWarnings("unchecked")
            List<String> authorities = claims.get("authorities", List.class);
            if (username != null) {
                UsernamePasswordAuthenticationToken auth
                        = new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        Optional.ofNullable(authorities)
                                .map(Collection::stream)
                                .map(v -> v.map(SimpleGrantedAuthority::new))
                                .map(v -> v.collect(Collectors.toList()))
                                .orElse(new ArrayList<>()));
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        filterChain.doFilter(req, res);
    }
}
