package com.cook.cancook.service;

import com.cook.cancook.dto.RatingDto;
import com.cook.cancook.mapper.RatingMapper;
import com.cook.cancook.model.RatingModel;
import com.cook.cancook.model.RecipesModel;
import com.cook.cancook.repository.RatingRepository;
import com.cook.cancook.repository.RecipesRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RatingService {

  @Autowired
  private RatingRepository ratingRepository;

  @Autowired
  private RatingMapper ratingMapper;

  @Autowired
  private RecipesRepository recipesRepository;

  public List<RatingDto> getAllRatings() {
    List<RatingModel> ratings = ratingRepository.findAll();
    return ratingMapper.toDtoList(ratings);
  }

  public Optional<RatingDto> getRatingById(Integer id) {
    return ratingRepository.findById(id).map(ratingMapper::toDto);
  }

  public List<RatingDto> getRatingsByRecipeId(Integer recipeId) {
    List<RatingModel> ratings = ratingRepository.findByRecipeId(recipeId);
    return ratingMapper.toDtoList(ratings);
  }

  public List<RatingDto> getRatingsByUserId(Integer userId) {
    List<RatingModel> ratings = ratingRepository.findByUserId(userId);
    return ratingMapper.toDtoList(ratings);
  }

  public Float getAverageRating(Integer recipeId) {
    return ratingRepository.getAverageRatingByRecipeId(recipeId);
  }

  public RatingDto submitRating(RatingDto dto) {
    // Check if user already rated this recipe
    Optional<RatingModel> existingRating =
        ratingRepository.findByRecipeIdAndUserId(dto.getRecipeId(), dto.getUserId());

    RatingModel rating;
    if (existingRating.isPresent()) {
      // Update existing rating
      rating = existingRating.get();
      rating.setRating(dto.getRating());
      rating.setCreatedAt(LocalDateTime.now());
    } else {
      // Create new rating
      rating = ratingMapper.toModel(dto);
      if (rating.getCreatedAt() == null) {
        rating.setCreatedAt(LocalDateTime.now());
      }
    }

    RatingModel savedRating = ratingRepository.save(rating);

    // Update recipe's average rating
    updateRecipeAverageRating(dto.getRecipeId());

    return ratingMapper.toDto(savedRating);
  }

  private void updateRecipeAverageRating(Integer recipeId) {
    Float avgRating = ratingRepository.getAverageRatingByRecipeId(recipeId);
    Optional<RecipesModel> recipe = recipesRepository.findById(recipeId);
    recipe.ifPresent(r -> {
      r.setRating(avgRating);
      recipesRepository.save(r);
    });
  }

  public boolean deleteRating(Integer id) {
    if (ratingRepository.existsById(id)) {
      Optional<RatingModel> rating = ratingRepository.findById(id);
      ratingRepository.deleteById(id);
      // Update recipe's average rating after deletion
      rating.ifPresent(r -> updateRecipeAverageRating(r.getRecipeId()));
      return true;
    }
    return false;
  }
}
