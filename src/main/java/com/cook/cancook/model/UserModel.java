package com.cook.cancook.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "User")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserModel {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(nullable = false, length = 8)
  private String username;

  @Column(nullable = false, length = 12)
  private String password;

  @Column(name = "login_attempts")
  private Integer loginAttempts;

  @Column
  private Integer status;

  @Column(name = "pantry_id")
  private Integer pantryId;

  @Column(length = 12)
  private String role;
}
