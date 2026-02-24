package com.cook.cancook.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

  private Integer id;
  private Integer recipeId;
  private Integer userId;
  private String commentText;
  private LocalDateTime createdAt;
  private String username; // Optional: for displaying who commented
}
