package com.cook.cancook.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeDetailDto {

  private Integer id;
  private Integer userId;
  private String name;
  private String image;
  private Boolean isPublic;
  private String instructions;
  private String timers;
  private Float rating;
  private List<RecipeIngredientsDto> ingredients;
}
