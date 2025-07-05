package com.osho.journalApp.service;

import com.osho.journalApp.entities.JournalEntry;
import com.osho.journalApp.entities.User;
import com.osho.journalApp.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JournalEntryServiceTest {

    @Mock
    private JournalEntryRepository journalEntryRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private JournalEntryService journalEntryService;

    private JournalEntry testEntry;
    private User testUser;
    private ObjectId entryId;

    @BeforeEach
    void setUp() {
        entryId = new ObjectId();
        testEntry = new JournalEntry();
        testEntry.setId(entryId);
        testEntry.setTitle("Test Entry");
        testEntry.setContent("Test Content");

        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setJournalEntries(new ArrayList<>());
    }

    @Test
    void save_ValidEntry_ReturnsSavedEntry() {
        when(userService.findByUserName("testuser")).thenReturn(testUser);
        when(journalEntryRepository.save(any(JournalEntry.class))).thenReturn(testEntry);

        JournalEntry savedEntry = journalEntryService.save("testuser", testEntry);

        assertNotNull(savedEntry);
        assertEquals(testEntry.getTitle(), savedEntry.getTitle());
        assertNotNull(savedEntry.getDate());
        verify(journalEntryRepository).save(any(JournalEntry.class));
        verify(userService).saveUser(testUser);
    }

    @Test
    void save_UserNotFound_ThrowsResponseStatusException() {
        when(userService.findByUserName("nonexistent")).thenReturn(null);

        assertThrows(ResponseStatusException.class, () ->
                journalEntryService.save("nonexistent", testEntry)
        );
    }

    @Test
    void deleteEntry_ExistingEntry_ReturnsTrue() {
        testUser.getJournalEntries().add(testEntry);
        when(userService.findByUserName("testuser")).thenReturn(testUser);

        boolean result = journalEntryService.deleteEntry(entryId, "testuser");

        assertTrue(result);
        verify(journalEntryRepository).deleteById(entryId);
        verify(userService).saveUser(testUser);
    }

    @Test
    void deleteEntry_NonExistingEntry_ReturnsFalse() {
        when(userService.findByUserName("testuser")).thenReturn(testUser);

        boolean result = journalEntryService.deleteEntry(new ObjectId(), "testuser");

        assertFalse(result);
        verify(journalEntryRepository, never()).deleteById(any());
    }

    @Test
    void findById_ExistingEntry_ReturnsEntry() {
        when(journalEntryRepository.findById(entryId)).thenReturn(Optional.of(testEntry));

        Optional<JournalEntry> found = journalEntryService.findById(entryId);

        assertTrue(found.isPresent());
        assertEquals(testEntry.getTitle(), found.get().getTitle());
        verify(journalEntryRepository).findById(entryId);
    }

    @Test
    void findById_NonExistingEntry_ReturnsEmpty() {
        when(journalEntryRepository.findById(any())).thenReturn(Optional.empty());

        Optional<JournalEntry> found = journalEntryService.findById(new ObjectId());

        assertFalse(found.isPresent());
        verify(journalEntryRepository).findById(any());
    }
}