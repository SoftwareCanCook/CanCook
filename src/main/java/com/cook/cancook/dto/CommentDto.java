package com.cook.cancook.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

  private Integer id;

  @JsonAlias({"recipe_id"})
  @JsonProperty("recipe_id")
  private Integer recipeId;

  @JsonAlias({"user_id"})
  @JsonProperty("user_id")
  private Integer userId;

  @JsonAlias({"comment_text"})
  @JsonProperty("comment_text")
  private String commentText;

  @JsonAlias({"created_at"})
  @JsonProperty("created_at")
  private LocalDateTime createdAt;

  @JsonProperty("username")
  private String username; // Optional: for displaying who commented

  @JsonAlias({"rating"})
  @JsonProperty("rating")
  private Float rating; // Rating for this comment
}
