package com.cook.cancook.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipePantryCheckDto {

  private Integer recipeId;
  private boolean canMake;
  private List<IngredientAvailabilityDto> ingredientStatus;
  private List<IngredientAvailabilityDto> missingIngredients;
}
