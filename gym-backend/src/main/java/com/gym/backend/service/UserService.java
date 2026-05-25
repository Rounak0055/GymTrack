package com.gym.backend.service;

import com.gym.backend.entity.User;
import com.gym.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Create
    public User addUser(User user) {
        return userRepository.save(user);
    }

    // Read
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Update
    public User updateUser(Long id, User newUser) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setName(newUser.getName());
                    user.setEmail(newUser.getEmail());
                    user.setPassword(newUser.getPassword());
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // Delete
    public String deleteUser(Long id) {
        userRepository.deleteById(id);
        return "User deleted successfully";


    }
}