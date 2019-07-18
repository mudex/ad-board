package com.shehab.adboard.userservice.resource;

import com.shehab.adboard.userservice.domain.User;
import com.shehab.adboard.userservice.repository.UserRepository;
import com.shehab.adboard.userservice.resource.exception.UserNotFountException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;

    @RequestMapping(value = "users/{userId}", method = RequestMethod.GET)
    public ResponseEntity<User> getUser(@PathVariable("userId") Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFountException(
                        String.format(
                                Constants.USER_NOT_FOUND,
                                userId.toString())
                ));
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @RequestMapping(value = "users", method = RequestMethod.GET)
    public ResponseEntity<List<User>> getAllUser() {
        return new ResponseEntity<>(
                userRepository.findAll(),
                HttpStatus.OK);
    }

    @RequestMapping(value = "/sign-up", method = RequestMethod.POST)
    public ResponseEntity<?> createUser(@RequestBody User user) {
        userRepository.save(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
