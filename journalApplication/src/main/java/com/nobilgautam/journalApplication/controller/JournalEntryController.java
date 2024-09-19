package com.nobilgautam.journalApplication.controller;

import com.nobilgautam.journalApplication.entity.JournalEntry;
import com.nobilgautam.journalApplication.entity.User;
import com.nobilgautam.journalApplication.service.JournalEntryService;
import com.nobilgautam.journalApplication.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    @Autowired
    public JournalEntryService journalEntryService;

    @Autowired
    public UserService userService;


    @GetMapping("{username}")
    public ResponseEntity<List<JournalEntry>> getJournalEntriesByUsername(@PathVariable String username) {
        User user = userService.getEntryByUsername(username);
        List<JournalEntry> list = user.getJournalEntries();
        if (!list.isEmpty()) {
            return new ResponseEntity<>(list, HttpStatus.OK);
        } else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("{username}")
    public ResponseEntity<JournalEntry> createJournalEntryByUsername(@RequestBody JournalEntry journalEntry, @PathVariable String username) {
        try {
                journalEntryService.saveEntry(journalEntry, username);
            return new ResponseEntity<>(journalEntry, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("id/{myId}")
    public ResponseEntity<JournalEntry> getJournalEntryById(@PathVariable ObjectId myId) {
        Optional<JournalEntry> entryById = journalEntryService.getEntryById(myId);
        if (entryById.isPresent()) {
            return new ResponseEntity<>(entryById.get(), HttpStatus.FOUND);
        } else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("id/{username}/{myId}")
    public ResponseEntity<JournalEntry> deleteJournalEntryById(@PathVariable ObjectId myId, @PathVariable String username) {
        journalEntryService.deleteEntryById(myId, username);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("id/{username}/{myId}")
    public ResponseEntity<JournalEntry> updateJournalById(@PathVariable ObjectId myId, @RequestBody JournalEntry journalEntry, @PathVariable String username) {
        JournalEntry old = journalEntryService.getEntryById(myId).orElse(null);
        if(old != null) {
            old.setTitle(journalEntry.getTitle() != null && !journalEntry.getTitle().isEmpty() ? journalEntry.getTitle() : old.getTitle());
            old.setContent(journalEntry.getContent() != null && !journalEntry.getContent().isEmpty() ? journalEntry.getContent() : old.getContent());
            journalEntryService.saveEntry(old);
            return new ResponseEntity<>(old, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
