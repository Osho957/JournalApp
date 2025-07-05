package com.osho.journalApp.controller;

import com.osho.journalApp.entities.User;
import com.osho.journalApp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/public")
public class PublicController {

    private final UserService userservice;
    @Autowired
    public PublicController(UserService userservice) {
        this.userservice = userservice;
    }

    @PostMapping("/create-users")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userservice.save(user);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }
}
