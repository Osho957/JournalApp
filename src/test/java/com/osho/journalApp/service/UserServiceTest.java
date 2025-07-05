package com.osho.journalApp.service;

import com.osho.journalApp.entities.User;
import com.osho.journalApp.repository.UserEntryRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserEntryRepository userEntryRepository;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private ObjectId userId;

    @BeforeEach
    void setUp() {
        userId = new ObjectId();
        testUser = new User();
        testUser.setId(userId);
        testUser.setUsername("tester");
        testUser.setPassword("password123");
    }

    @Test
    void save_ValidUser_ReturnsSavedUser() {
        when(userEntryRepository.save(any(User.class))).thenReturn(testUser);

        User savedUser = userService.save(testUser);

        assertNotNull(savedUser);
        assertEquals(testUser.getUsername(), savedUser.getUsername());
        assertTrue(savedUser.getRoles().contains("USER"));
        verify(userEntryRepository).save(any(User.class));
    }

    @Test
    void save_NullUser_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> userService.save(null));
    }

    @Test
    void findAll_ReturnsAllUsers() {
        List<User> users = Collections.singletonList(testUser);
        when(userEntryRepository.findAll()).thenReturn(users);

        List<User> result = userService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(userEntryRepository).findAll();
    }

    @Test
    void findById_ExistingId_ReturnsUser() {
        when(userEntryRepository.findById(userId)).thenReturn(Optional.of(testUser));

        User found = userService.findById(userId);

        assertNotNull(found);
        assertEquals(testUser.getUsername(), found.getUsername());
        verify(userEntryRepository).findById(userId);
    }

    @Test
    void findById_NonExistingId_ReturnsNull() {
        when(userEntryRepository.findById(any())).thenReturn(Optional.empty());

        User found = userService.findById(new ObjectId());

        assertNull(found);
        verify(userEntryRepository).findById(any());
    }

    @Test
    void deleteEntry_ExistingId_ReturnsTrue() {
        when(userEntryRepository.existsById(userId)).thenReturn(true);

        boolean result = userService.deleteEntry(userId);

        assertTrue(result);
        verify(userEntryRepository).deleteById(userId);
    }

    @Test
    void deleteEntry_NonExistingId_ReturnsFalse() {
        when(userEntryRepository.existsById(any())).thenReturn(false);

        boolean result = userService.deleteEntry(new ObjectId());

        assertFalse(result);
        verify(userEntryRepository, never()).deleteById(any());
    }

    @Test
    void findByUserName_ExistingUsername_ReturnsUser() {
        when(userEntryRepository.findByUsername("tester")).thenReturn(testUser);

        User found = userService.findByUserName("tester");

        assertNotNull(found);
        assertEquals(testUser.getUsername(), found.getUsername());
        verify(userEntryRepository).findByUsername("tester");
    }

    @Test
    void saveAdmin_ValidUser_ReturnsSavedAdminUser() {
        when(userEntryRepository.save(any(User.class))).thenReturn(testUser);

        User savedUser = userService.saveAdmin(testUser);

        assertNotNull(savedUser);
        assertTrue(savedUser.getRoles().contains("ADMIN"));
        assertTrue(savedUser.getRoles().contains("USER"));
        verify(userEntryRepository).save(any(User.class));
    }
}