package com.shehab.adboard.userservice.resource.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;

public class UserNotFountException extends HttpClientErrorException {
    public UserNotFountException(String statusText) {
        super(HttpStatus.NOT_FOUND, statusText);
    }
}
