package com.cook.cancook.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IngredientAvailabilityDto {

  private Integer itemId;
  private String itemName;
  private Integer quantityNeeded;
  private String measurementUnit;
  private Integer quantityAvailable;
  private boolean isAvailable;
  private Integer quantityMissing;
}
