package com.cook.cancook.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreDto {

  private Integer id;
  private String name;
  private String address;
  private String city;
  private String state;
  private String zipCode;
  private String phone;
  private LocalDateTime createdAt;
}
