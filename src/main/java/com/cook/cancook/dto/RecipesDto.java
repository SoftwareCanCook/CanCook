package com.cook.cancook.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipesDto {

  private Integer id;
  private Integer userId;
  private String name;
  private String imageUrl;
  private Boolean isPublic;
  private String instructionsAndTimers;
  private Float rating;
}
