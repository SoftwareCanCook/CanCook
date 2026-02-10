package com.cook.cancook.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

  private Integer id;
  private String username;
  private String password;
  private Integer loginAttempts;
  private Integer status;
  private Integer pantryId;
  private String role;
}
