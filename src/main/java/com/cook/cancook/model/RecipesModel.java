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

  @Column(name = "image_url")
  private String imageUrl;

  @Column(name = "is_public")
  private Boolean isPublic;

  @Column(name = "instructions_and_timers", columnDefinition = "TEXT")
  private String instructionsAndTimers;

  @Column
  private Float rating;
}
