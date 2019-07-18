package com.shehab.adboard.userservice.security;

import com.shehab.adboard.userservice.domain.User;
import com.shehab.adboard.userservice.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secret;
    @Autowired
    private UserDetailsService customUserDetailsService;

    public UserDetails parseToken(String token){
        try {
            Claims claims = Jwts
                    .parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token).getBody();
            return customUserDetailsService
                    .loadUserByUsername(claims.getSubject());
        }catch (JwtException e){
            return null;
        }

    }
    public String generateJwtToken(UserDetails u){
        Claims claims = Jwts.claims().setSubject(u.getUsername());
//        claims.put("userName", u.getUsername() + "");

        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }


}
