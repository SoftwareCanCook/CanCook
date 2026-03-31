package com.cook.cancook.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RatingDto {

  private Integer id;

  @JsonAlias({"recipe_id"})
  private Integer recipeId;

  @JsonAlias({"user_id"})
  private Integer userId;

  @JsonAlias({"rating_value", "value", "score"})
  private Float rating;

  @JsonAlias({"created_at"})
  private LocalDateTime createdAt;
}
