package com.cook.cancook.repository;

import com.cook.cancook.model.RecipesModel;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipesRepository extends JpaRepository<RecipesModel, Integer> {

  List<RecipesModel> findByUserId(Integer userId);

  List<RecipesModel> findByIsPublic(Boolean isPublic);

  List<RecipesModel> findByIsPublic(Boolean isPublic, Sort sort);

  List<RecipesModel> findByNameContainingIgnoreCase(String name);

  List<RecipesModel> findByNameContainingIgnoreCase(String name, Sort sort);

  List<RecipesModel> findByRatingGreaterThanEqual(Float rating);

  List<RecipesModel> findByRatingGreaterThanEqual(Float rating, Sort sort);

  List<RecipesModel> findAll(Sort sort);
}
