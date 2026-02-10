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
@Table(name = "Recipe_Ingredients")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeIngredientsModel {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "recipe_id")
  private Integer recipeId;

  @Column(name = "item_id")
  private Integer itemId;

  @Column(name = "quantity_needed")
  private Integer quantityNeeded;

  @Column(name = "measurement_unit")
  private String measurementUnit;
}
