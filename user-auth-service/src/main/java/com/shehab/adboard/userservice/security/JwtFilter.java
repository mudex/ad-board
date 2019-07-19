package com.shehab.adboard.userservice.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        UserDetails userDetails = null;
        String authHeader = req.getHeader("Authorization");
        if(Objects.nonNull(authHeader) && authHeader.startsWith("Bearer ")){
            userDetails = jwtUtil
                    .parseToken(authHeader.substring(7));
        }
        if(Objects.nonNull(userDetails) &&
                Objects.isNull(SecurityContextHolder.getContext().getAuthentication())){
            UsernamePasswordAuthenticationToken token =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(token);

        }

        filterChain.doFilter(req,res);
    }
}
