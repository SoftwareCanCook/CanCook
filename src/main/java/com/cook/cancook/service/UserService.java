package com.cook.cancook.service;

import com.cook.cancook.dto.UserDto;
import com.cook.cancook.mapper.UserMapper;
import com.cook.cancook.model.UserModel;
import com.cook.cancook.repository.UserReposity;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {

  @Autowired
  private UserReposity userRepository;

  @Autowired
  private UserMapper userMapper;

  public List<UserDto> getAllUsers() {
    List<UserModel> users = userRepository.findAll();
    return userMapper.toDtoList(users);
  }

  public Optional<UserDto> getUserById(Integer id) {
    return userRepository.findById(id).map(userMapper::toDto);
  }

  public Optional<UserDto> getUserByUsername(String username) {
    return userRepository.findByUsername(username).map(userMapper::toDto);
  }

  public Optional<UserDto> getUserByPantryId(Integer pantryId) {
    return userRepository.findByPantryId(pantryId).map(userMapper::toDto);
  }

  public UserDto createUser(UserDto userDto) {
    if (userRepository.existsByUsername(userDto.getUsername())) {
      throw new RuntimeException("Username already exists");
    }
    UserModel user = userMapper.toModel(userDto);
    UserModel savedUser = userRepository.save(user);
    return userMapper.toDto(savedUser);
  }

  public Optional<UserDto> updateUser(Integer id, UserDto userDto) {
    return userRepository.findById(id).map(existingUser -> {
      if (userDto.getUsername() != null
          && !userDto.getUsername().equals(existingUser.getUsername())) {
        if (userRepository.existsByUsername(userDto.getUsername())) {
          throw new RuntimeException("Username already exists");
        }
        existingUser.setUsername(userDto.getUsername());
      }
      if (userDto.getPassword() != null) {
        existingUser.setPassword(userDto.getPassword());
      }
      if (userDto.getLoginAttempts() != null) {
        existingUser.setLoginAttempts(userDto.getLoginAttempts());
      }
      if (userDto.getStatus() != null) {
        existingUser.setStatus(userDto.getStatus());
      }
      if (userDto.getPantryId() != null) {
        existingUser.setPantryId(userDto.getPantryId());
      }
      if (userDto.getRole() != null) {
        existingUser.setRole(userDto.getRole());
      }
      UserModel updatedUser = userRepository.save(existingUser);
      return userMapper.toDto(updatedUser);
    });
  }

  public boolean deleteUser(Integer id) {
    if (userRepository.existsById(id)) {
      userRepository.deleteById(id);
      return true;
    }
    return false;
  }
}
