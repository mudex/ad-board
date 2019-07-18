package com.shehab.adboard.userservice.domain;

import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userName;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private Boolean accountNonExpired = true;
    private Boolean accountNonLocked = true;
    private Boolean CredentialsNonExpired = true;
    private Boolean enabled = true;
}
