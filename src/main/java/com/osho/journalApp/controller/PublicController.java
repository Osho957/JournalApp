package com.osho.journalApp.controller;

import com.osho.journalApp.entities.User;
import com.osho.journalApp.scheduler.UserScheduler;
import com.osho.journalApp.service.EmailService;
import com.osho.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
public class PublicController {

    private final UserService userservice;
    private final UserScheduler userScheduler;
    @Autowired
    public PublicController(UserService userservice, UserScheduler userScheduler) {
        this.userservice = userservice;
        this.userScheduler = userScheduler;
    }

    @PostMapping("/create-users")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userservice.save(user);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PostMapping
    public String sendMail(@RequestParam String email, @RequestParam String subject, @RequestParam String body) {
        try {
            userScheduler.fetchUsersAndSendMail();
            return "Email sent successfully";
        } catch (Exception e) {
            return "Failed to send email: " + e.getMessage();
        }
    }
}
