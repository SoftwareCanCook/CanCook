package com.cook.cancook.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RatingDto {

  private Integer id;
  private Integer recipeId;
  private Integer userId;
  private Float rating;
  private LocalDateTime createdAt;
}
