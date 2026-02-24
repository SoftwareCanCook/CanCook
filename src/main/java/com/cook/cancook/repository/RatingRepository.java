package com.cook.cancook.repository;

import com.cook.cancook.model.RatingModel;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepository extends JpaRepository<RatingModel, Integer> {

  List<RatingModel> findByRecipeId(Integer recipeId);

  List<RatingModel> findByUserId(Integer userId);

  Optional<RatingModel> findByRecipeIdAndUserId(Integer recipeId, Integer userId);

  @Query("SELECT AVG(r.rating) FROM RatingModel r WHERE r.recipeId = :recipeId")
  Float getAverageRatingByRecipeId(Integer recipeId);
}
