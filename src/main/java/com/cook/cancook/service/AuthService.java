package com.cook.cancook.service;

import com.cook.cancook.dto.LoginRequest;
import com.cook.cancook.dto.LoginResponse;
import com.cook.cancook.dto.RegisterRequest;
import com.cook.cancook.dto.RegisterResponse;
import com.cook.cancook.dto.UserDto;
import com.cook.cancook.mapper.UserMapper;
import com.cook.cancook.model.UserModel;
import com.cook.cancook.repository.UserReposity;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthService {

  @Autowired
  private UserReposity userRepository;

  @Autowired
  private UserMapper userMapper;

  private static final int MAX_LOGIN_ATTEMPTS = 5;

  public LoginResponse login(LoginRequest request) {
    Optional<UserModel> userOptional = userRepository.findByUsername(request.getUsername());

    if (!userOptional.isPresent()) {
      return new LoginResponse(false, "User not found", null, null);
    }

    UserModel user = userOptional.get();

    // Check if account is locked
    if (user.getStatus() == 0) {
      return new LoginResponse(
          false, "Account is locked. Please contact administrator.", null, null);
    }

    // Check if login attempts exceeded
    if (user.getLoginAttempts() != null && user.getLoginAttempts() >= MAX_LOGIN_ATTEMPTS) {
      user.setStatus(0); // Lock the account
      userRepository.save(user);
      return new LoginResponse(
          false, "Account locked due to too many failed login attempts.", null, null);
    }

    // Verify password
    if (!user.getPassword().equals(request.getPassword())) {
      // Increment login attempts
      int attempts = user.getLoginAttempts() != null ? user.getLoginAttempts() : 0;
      user.setLoginAttempts(attempts + 1);
      userRepository.save(user);

      int remainingAttempts = MAX_LOGIN_ATTEMPTS - user.getLoginAttempts();
      return new LoginResponse(
          false, "Invalid password. " + remainingAttempts + " attempts remaining.", null, null);
    }

    // Successful login - reset login attempts
    user.setLoginAttempts(0);
    userRepository.save(user);

    UserDto userDto = userMapper.toDto(user);
    // Don't send password back
    userDto.setPassword(null);

    return new LoginResponse(true, "Login successful", userDto, "mock-token-" + user.getId());
  }

  public boolean logout(Integer userId) {
    // In a real implementation, this would invalidate the session/token
    // For now, just verify user exists
    return userRepository.existsById(userId);
  }

  public boolean resetLoginAttempts(Integer userId) {
    Optional<UserModel> userOptional = userRepository.findById(userId);
    if (userOptional.isPresent()) {
      UserModel user = userOptional.get();
      user.setLoginAttempts(0);
      user.setStatus(1); // Unlock account
      userRepository.save(user);
      return true;
    }
    return false;
  }

  public RegisterResponse register(RegisterRequest request) {
    // Validate input
    if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
      return new RegisterResponse(false, "Username is required", null);
    }
    if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
      return new RegisterResponse(false, "Password is required", null);
    }

    // Check username length (max 8 characters as per schema)
    if (request.getUsername().length() > 8) {
      return new RegisterResponse(false, "Username must be 8 characters or less", null);
    }

    // Check password length (max 12 characters as per schema)
    if (request.getPassword().length() > 12) {
      return new RegisterResponse(false, "Password must be 12 characters or less", null);
    }

    // Check if username already exists
    if (userRepository.existsByUsername(request.getUsername())) {
      return new RegisterResponse(false, "Username already exists", null);
    }

    // Create new user with defaults
    UserModel newUser = new UserModel();
    newUser.setUsername(request.getUsername());
    newUser.setPassword(request.getPassword());
    newUser.setLoginAttempts(0);
    newUser.setStatus(1); // Active
    newUser.setRole("user"); // Default role

    UserModel savedUser = userRepository.save(newUser);
    UserDto userDto = userMapper.toDto(savedUser);
    // Don't send password back
    userDto.setPassword(null);

    return new RegisterResponse(true, "Registration successful", userDto);
  }
}
