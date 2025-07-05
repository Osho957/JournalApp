package com.osho.journalApp.service;

import com.osho.journalApp.entities.User;
import com.osho.journalApp.repository.UserEntryRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class UserService {


    private final UserEntryRepository userEntryRepository;
    private static final BCryptPasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    @Autowired
    public UserService(UserEntryRepository userEntryRepository) {
        this.userEntryRepository = userEntryRepository;
    }

    public User save(User user) {
        if (user != null) {
            user.setPassword(PASSWORD_ENCODER.encode(user.getPassword()));
            user.setRoles(Collections.singletonList("USER"));
            return userEntryRepository.save(user);
        } else {
            throw new IllegalArgumentException("Journal entry cannot be null");
        }
    }

    public void saveUser(User user) {
        userEntryRepository.save(user);
    }

    public List<User> findAll() {
        return userEntryRepository.findAll();
    }

    public User findById(ObjectId id) {
        return userEntryRepository.findById(id).orElse(null);
    }

    public boolean deleteEntry(ObjectId id) {
        if (userEntryRepository.existsById(id)) {
            userEntryRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public User findByUserName(String username) {
        return userEntryRepository.findByUsername(username);
    }

    public void deleteByUsername(String username) {
        userEntryRepository.deleteByUsername(username);
    }

    public User saveAdmin(User user) {
        if (user != null) {
            user.setPassword(PASSWORD_ENCODER.encode(user.getPassword()));
            user.setRoles(Arrays.asList("USER", "ADMIN"));
            return userEntryRepository.save(user);
        } else {
            throw new IllegalArgumentException("Journal entry cannot be null");
        }
    }
}
