package com.shehab.adboard.userservice.service;

import com.shehab.adboard.userservice.domain.CustomUserDetails;
import com.shehab.adboard.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        return userRepository
                .findByUserName(userName)
                .map(user -> CustomUserDetails.builder()
                        .accountNonExpired(user.getAccountNonExpired())
                        .accountNonLocked(user.getAccountNonLocked())
                        .CredentialsNonExpired(user.getCredentialsNonExpired())
                        .enabled(user.getEnabled())
                        .userName(user.getUserName())
                        .password(user.getPassword())
                        .build()
                )
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format("User '%s' Not Found",userName)
                ));
    }
}
