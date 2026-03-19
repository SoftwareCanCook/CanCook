package com.cook.cancook.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeIngredientInputDto {

  private Integer itemId;
  private String name;
  private String ingredientName;
  private Integer quantityNeeded;
  private Integer quantity;
  private String measurementUnit;
  private String unit;
}
