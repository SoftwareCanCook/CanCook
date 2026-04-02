package com.cook.cancook.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Stores")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreModel {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(nullable = false)
  private String name;

  @Column(length = 500)
  private String address;

  @Column(length = 100)
  private String city;

  @Column(length = 50)
  private String state;

  @Column(name = "zip_code", length = 20)
  private String zipCode;

  @Column(length = 20)
  private String phone;

  @Column(name = "image_url", length = 1000)
  private String imageUrl;

  @Column(name = "created_at")
  private LocalDateTime createdAt;
}
