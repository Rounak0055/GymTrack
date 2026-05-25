package com.gym.backend.controller;

import com.gym.backend.entity.User;
import com.gym.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")   // ✅ Allow React frontend to call this
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    // ✅ REGISTER
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            // Check if email already exists
            if (user.getEmail() == null || user.getEmail().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Email is required"));
            }
            if (user.getPassword() == null || user.getPassword().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Password is required"));
            }

            User existing = userRepository.findByEmail(user.getEmail());
            if (existing != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("error", "Email already registered"));
            }

            User saved = userRepository.save(user);

            // Return clean response (no password)
            Map<String, Object> response = new HashMap<>();
            response.put("id", saved.getId());
            response.put("name", saved.getName());
            response.put("email", saved.getEmail());
            response.put("message", "Registration successful");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Registration failed: " + e.getMessage()));
        }
    }

    // ✅ LOGIN - Fixed 500 error
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        try {
            // Null checks
            if (user.getEmail() == null || user.getEmail().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Email is required"));
            }
            if (user.getPassword() == null || user.getPassword().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Password is required"));
            }

            // Find user by email
            User existingUser = userRepository.findByEmail(user.getEmail());

            if (existingUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "No account found with this email"));
            }

            // Check password
            if (!existingUser.getPassword().equals(user.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Incorrect password"));
            }

            // ✅ Return userId + user info (used by React to store in localStorage)
            Map<String, Object> response = new HashMap<>();
            response.put("id", existingUser.getId());
            response.put("name", existingUser.getName());
            response.put("email", existingUser.getEmail());
            response.put("message", "Login successful");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Login failed: " + e.getMessage()));
        }
    }
}
