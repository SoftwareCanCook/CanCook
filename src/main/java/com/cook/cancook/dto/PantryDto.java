package com.cook.cancook.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PantryDto {

  private Integer id;
  private Integer userId;
  private Integer itemId;
  private Integer quantity;
}
