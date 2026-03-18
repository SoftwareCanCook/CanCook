package com.cook.cancook.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Recipes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipesModel {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "user_id")
  private Integer userId;

  @Column
  private String name;

  @Column(length = 1000)
  private String image;

  @Column(name = "is_public")
  private Boolean isPublic;

  @Column(name = "instructions", columnDefinition = "TEXT")
  private String instructions;

  @Column(name = "timers", columnDefinition = "TEXT")
  private String timers;

  @Column
  private Float rating;
}
