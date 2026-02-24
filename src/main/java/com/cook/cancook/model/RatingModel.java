package com.cook.cancook.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Ratings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RatingModel {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "recipe_id", nullable = false)
  private Integer recipeId;

  @Column(name = "user_id", nullable = false)
  private Integer userId;

  @Column(nullable = false)
  private Float rating;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;
}
