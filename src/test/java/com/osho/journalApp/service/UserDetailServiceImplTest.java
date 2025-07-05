package com.osho.journalApp.service;

import com.osho.journalApp.entities.User;
import com.osho.journalApp.repository.UserEntryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;

import static org.mockito.Mockito.when;

public class UserDetailServiceImplTest {

    @InjectMocks
    private UserDetailServiceImpl userDetailService;

    @Mock
    private UserEntryRepository userEntryRepository;

    @BeforeEach
    void setUp() {
       MockitoAnnotations.openMocks(this);
    }

    @Test
    void loadByUsernameTest() {
       when(userEntryRepository.findByUsername(ArgumentMatchers.anyString())).thenReturn(User.builder().username("testUser")
                .password("testPassword")
                .roles(Collections.singletonList("USER"))
                .build());
        UserDetails userDetails = userDetailService.loadUserByUsername("testUser");
        Assertions.assertNotNull(userDetails);

    }
}
