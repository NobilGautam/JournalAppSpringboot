package com.nobilgautam.journalApplication.controller;

import com.nobilgautam.journalApplication.entity.JournalEntry;
import com.nobilgautam.journalApplication.entity.User;
import com.nobilgautam.journalApplication.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    public UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getAll() {
        List<User> list = userService.getEntries();
        if (!list.isEmpty()) {
            return new ResponseEntity<>(list, HttpStatus.OK);
        } else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<User> createEntry(@RequestBody User user) {
        try {
            userService.saveEntry(user);
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("id/{myId}")
    public ResponseEntity<User> getJournalEntryById(@PathVariable ObjectId myId) {
        Optional<User> entryById = userService.getEntryById(myId);
        if (entryById.isPresent()) {
            return new ResponseEntity<>(entryById.get(), HttpStatus.FOUND);
        } else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{username}")
    public ResponseEntity<User> updateJournalById(@PathVariable String username, @RequestBody User user) {
        User old = userService.userRepository.findByUsername(username);
        if(old != null) {
            old.setUsername(user.getUsername() != null && !user.getUsername().isEmpty() ? user.getUsername() : old.getUsername());
            old.setPassword(user.getPassword() != null && !user.getPassword().isEmpty() ? user.getPassword() : old.getPassword());
            userService.saveEntry(old);
            return new ResponseEntity<>(old, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
