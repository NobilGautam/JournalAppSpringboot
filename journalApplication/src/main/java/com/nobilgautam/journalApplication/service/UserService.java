package com.nobilgautam.journalApplication.service;

import com.nobilgautam.journalApplication.entity.JournalEntry;
import com.nobilgautam.journalApplication.entity.User;
import com.nobilgautam.journalApplication.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserService {
    @Autowired
    public UserRepository userRepository;

    public void saveEntry(User user) {
        userRepository.save(user);
    }

    public List<User> getEntries() {
        return userRepository.findAll();
    }

    public Optional<User> getEntryById(ObjectId id) {
        return userRepository.findById(id);
    }

    public void deleteEntryById(ObjectId id) {
        userRepository.deleteById(id);
    }

    public User getEntryByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
