package com.shehab.adboard.userservice.resource;

import com.shehab.adboard.userservice.resource.model.UserLogin;
import com.shehab.adboard.userservice.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService customUserDetailsService;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public String login(@RequestBody UserLogin userLogin) throws Exception {
        authenticate(userLogin);
        return jwtUtil.generateJwtToken(
                customUserDetailsService.loadUserByUsername(
                        userLogin.getUserName()
                )
        );

    }

    private void authenticate(@RequestBody UserLogin userLogin) throws Exception {
       try{
           authenticationManager.authenticate(
                   new UsernamePasswordAuthenticationToken(
                           userLogin.getUserName(),
                           userLogin.getPassword())
           );
       }catch (AuthenticationException e){
           throw new Exception(e);
       }
    }
}
