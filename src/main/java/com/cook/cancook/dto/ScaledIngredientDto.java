package com.cook.cancook.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScaledIngredientDto {

  private Integer itemId;
  private String itemName;
  private Integer originalQuantity;
  private Double scaledQuantity;
  private String measurementUnit;
}
