package com.cook.cancook.controller;

import com.cook.cancook.dto.LoginRequest;
import com.cook.cancook.dto.RegisterRequest;
import com.cook.cancook.model.UserModel;
import com.cook.cancook.repository.UserReposity;
import com.cook.cancook.util.JwtTokenUtil;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Authentication Controller Handles user login and signup
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

  @Autowired
  private UserReposity userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private JwtTokenUtil jwtTokenUtil;

  /** User login endpoint POST /api/auth/login */
  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody LoginRequest request) {
    try {
      // Find user by username
      Optional<UserModel> userOptional = userRepository.findByUsername(request.getUsername());

      if (!userOptional.isPresent()) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(Map.of("message", "Invalid credentials"));
      }

      UserModel user = userOptional.get();

      // Check if account is locked
      if (user.getStatus() == 0) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(Map.of("message", "Account is locked. Please contact administrator."));
      }

      // Verify password
      if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
        // Increment login attempts
        int attempts = user.getLoginAttempts() != null ? user.getLoginAttempts() : 0;
        user.setLoginAttempts(attempts + 1);

        // Lock account if max attempts exceeded
        if (user.getLoginAttempts() >= 5) {
          user.setStatus(0);
          userRepository.save(user);
          return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
              .body(Map.of("message", "Account locked due to too many failed login attempts."));
        }

        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(Map.of("message", "Invalid credentials"));
      }

      // Reset login attempts on successful login
      user.setLoginAttempts(0);
      userRepository.save(user);

      // Generate JWT token
      String token = jwtTokenUtil.generateToken(user.getUsername());

      // Return response
      Map<String, Object> response = new HashMap<>();
      response.put("token", token);
      response.put(
          "user",
          Map.of(
              "id", user.getId(),
              "username", user.getUsername(),
              "email", user.getEmail() != null ? user.getEmail() : ""));

      return ResponseEntity.ok(response);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(Map.of("message", "Login failed: " + e.getMessage()));
    }
  }

  /** User signup endpoint POST /api/auth/signup */
  @PostMapping("/signup")
  public ResponseEntity<?> signup(@RequestBody RegisterRequest request) {
    try {
      // Validate input
      if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(Map.of("message", "Username is required"));
      }
      if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(Map.of("message", "Password is required"));
      }

      // Check if username already exists
      if (userRepository.existsByUsername(request.getUsername())) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(Map.of("message", "Username already exists"));
      }

      // Check if email already exists (if provided)
      String email = request.getEmail();
      if (email != null && !email.trim().isEmpty()) {
        if (userRepository.existsByEmail(email)) {
          return ResponseEntity.status(HttpStatus.BAD_REQUEST)
              .body(Map.of("message", "Email already exists"));
        }
      } else {
        // Generate default email if not provided
        email = request.getUsername() + "@cancook.local";
      }

      // Create new user
      UserModel user = new UserModel();
      user.setUsername(request.getUsername());
      user.setEmail(email);
      user.setPassword(passwordEncoder.encode(request.getPassword()));
      user.setLoginAttempts(0);
      user.setStatus(1); // Active
      user.setRole("user"); // Default role

      // Save user
      user = userRepository.save(user);

      // Generate JWT token
      String token = jwtTokenUtil.generateToken(user.getUsername());

      // Return response
      Map<String, Object> response = new HashMap<>();
      response.put("token", token);
      response.put(
          "user",
          Map.of("id", user.getId(), "username", user.getUsername(), "email", user.getEmail()));

      return ResponseEntity.status(HttpStatus.CREATED).body(response);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(Map.of("message", "Signup failed: " + e.getMessage()));
    }
  }

  /** Alternative registration endpoint for backwards compatibility */
  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
    return signup(request);
  }

  /** Logout endpoint (optional - JWT is stateless) POST /api/auth/logout */
  @PostMapping("/logout")
  public ResponseEntity<?> logout() {
    // With JWT, logout is handled on client side by removing the token
    return ResponseEntity.ok(Map.of("message", "Logout successful"));
  }
}
