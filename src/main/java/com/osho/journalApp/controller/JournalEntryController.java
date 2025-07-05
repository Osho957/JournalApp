package com.osho.journalApp.controller;

import com.osho.journalApp.entities.JournalEntry;
import com.osho.journalApp.entities.User;
import com.osho.journalApp.service.JournalEntryService;
import com.osho.journalApp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    private final JournalEntryService journalEntryService;
    private final UserService userService;

    @Autowired
    public JournalEntryController(JournalEntryService journalEntryService, UserService userService) {
        this.journalEntryService = journalEntryService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<JournalEntry>> getAllEntriesOfUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUserName(auth.getName());
        List<JournalEntry> entries = user.getJournalEntries();
        return ResponseEntity.ok(entries);
    }

    @PostMapping
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry entry) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        JournalEntry savedEntry = journalEntryService.save(auth.getName(), entry);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEntry);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<JournalEntry> getEntryById(@PathVariable ObjectId id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userService.findByUserName(username);
       List<JournalEntry>entries =  user.getJournalEntries().stream().filter(entity -> entity.getId().equals(id)).collect(Collectors.toList());
       if(!entries.isEmpty()) {
           Optional<JournalEntry> entry = journalEntryService.findById(id);
           if (entry.isPresent()) {
               return new ResponseEntity<>(entry.get(), HttpStatus.OK);
           }
       }
       return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/id/{id}")
    public ResponseEntity<JournalEntry> updateEntry(@PathVariable ObjectId id, @RequestBody JournalEntry entry) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userService.findByUserName(username);
        List<JournalEntry>entries =  user.getJournalEntries().stream().filter(entity -> entity.getId().equals(id)).collect(Collectors.toList());
        if (!entries.isEmpty()) {
            Optional<JournalEntry> journalEntryOptional = journalEntryService.findById(id);
            if (journalEntryOptional.isPresent()) {
                JournalEntry journalEntry = journalEntryOptional.get();
                if (!entry.getTitle().isEmpty()) {
                    journalEntry.setTitle(entry.getTitle());
                }
                if (entry.getContent() != null && !entry.getContent().isEmpty()) {
                    journalEntry.setContent(entry.getContent());
                }
                journalEntryService.save(journalEntry);
                return new ResponseEntity<>(journalEntry, HttpStatus.OK);
            }
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<Void> deleteEntry(@PathVariable ObjectId id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean deleted = journalEntryService.deleteEntry(id, auth.getName());
        return deleted
                ? ResponseEntity.noContent().build()
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
