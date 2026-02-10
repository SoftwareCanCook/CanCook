package com.cook.cancook.repository;

import com.cook.cancook.model.RecipesModel;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipesRepository extends JpaRepository<RecipesModel, Integer> {

  List<RecipesModel> findByUserId(Integer userId);

  List<RecipesModel> findByIsPublic(Boolean isPublic);

  List<RecipesModel> findByNameContainingIgnoreCase(String name);

  List<RecipesModel> findByRatingGreaterThanEqual(Float rating);
}
