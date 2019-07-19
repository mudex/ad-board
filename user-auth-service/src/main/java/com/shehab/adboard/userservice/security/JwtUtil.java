package com.shehab.adboard.userservice.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expired-after:15}")
    private Long expiredAfter;
    @Autowired
    private UserDetailsService customUserDetailsService;

    private Claims getClaims(String token){
        try{
            return Jwts
                    .parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token).getBody();
        } catch (Exception e){
            return null;
        }
    }

    public UserDetails parseToken(String token){
        return Optional.ofNullable(getClaims(token))
                .map(c -> customUserDetailsService
                        .loadUserByUsername(c.getSubject()))
                .orElse(null);

    }

    public String generateJwtToken(UserDetails u){
        Claims claims = Jwts.claims().setSubject(u.getUsername());
        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, secret)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiredAfter*60*1000))
                .compact();
    }

    public Boolean validateToken(String token){
        Claims claims = getClaims(token);
        return Objects.nonNull(parseToken(token)) &&
                Objects.nonNull(claims) &&
                claims.getExpiration().after(new Date(System.currentTimeMillis()));
    }

}
