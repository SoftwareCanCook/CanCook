package com.cook.cancook.controller;

import com.cook.cancook.dto.UserDto;
import com.cook.cancook.service.UserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

  @Autowired
  private UserService userService;

  @GetMapping
  public ResponseEntity<List<UserDto>> getAllUsers() {
    List<UserDto> users = userService.getAllUsers();
    return ResponseEntity.ok(users);
  }

  @GetMapping("/{id}")
  public ResponseEntity<UserDto> getUserById(@PathVariable Integer id) {
    return userService
        .getUserById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/username/{username}")
  public ResponseEntity<UserDto> getUserByUsername(@PathVariable String username) {
    return userService
        .getUserByUsername(username)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<?> createUser(@RequestBody UserDto userDto) {
    try {
      // Set defaults if not provided
      if (userDto.getLoginAttempts() == null) {
        userDto.setLoginAttempts(0);
      }
      if (userDto.getStatus() == null) {
        userDto.setStatus(1); // Active by default
      }
      if (userDto.getRole() == null || userDto.getRole().isEmpty()) {
        userDto.setRole("user"); // Default role
      }
      if (userDto.getEmail() == null || userDto.getEmail().isEmpty()) {
        userDto.setEmail(userDto.getUsername() + "@cancook.local"); // Default email
      }

      UserDto createdUser = userService.createUser(userDto);
      return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<UserDto> updateUser(
      @PathVariable Integer id, @RequestBody UserDto userDto) {
    try {
      return userService
          .updateUser(id, userDto)
          .map(ResponseEntity::ok)
          .orElse(ResponseEntity.notFound().build());
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest().build();
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
    if (userService.deleteUser(id)) {
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.notFound().build();
  }
}
