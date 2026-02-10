package com.cook.cancook.repository;

import com.cook.cancook.model.RecipeIngredientsModel;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeIngredientsRepository
    extends JpaRepository<RecipeIngredientsModel, Integer> {

  List<RecipeIngredientsModel> findByRecipeId(Integer recipeId);

  List<RecipeIngredientsModel> findByItemId(Integer itemId);

  void deleteByRecipeId(Integer recipeId);
}
