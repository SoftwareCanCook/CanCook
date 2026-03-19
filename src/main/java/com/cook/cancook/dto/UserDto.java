package com.cook.cancook.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

  private Integer id;
  private String username;
  private String email;
  private String password;
  
  @JsonProperty("login_attempts")
  private Integer loginAttempts;
  
  private Integer status;
  private String role;
  
  @JsonProperty("store_id")
  private Integer storeId;
}
