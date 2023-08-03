package com.nailton.UserAuth.service;

import com.nailton.UserAuth.model.User;
import com.nailton.UserAuth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void insertUser(User user) {
        this.userRepository.save(user);
    }

    public List<User> listUsers() {
        return this.userRepository.findAll();
    }

    public User listUserById(UUID id) {
        return this.userRepository.findById(id)
                .stream().findFirst().orElse(null);
    }

    public User updateUser(User user) {
        this.userRepository.saveAndFlush(user);
        return user;
    }

    public void deleteUser(UUID id) {
        this.userRepository.deleteById(id);
    }

    public void deleteAll() {
        this.userRepository.deleteAll();
    }
}
