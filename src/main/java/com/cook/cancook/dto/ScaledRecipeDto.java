package com.cook.cancook.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScaledRecipeDto {

  private Integer recipeId;
  private Double scaleFactor;
  private List<ScaledIngredientDto> scaledIngredients;
}
