package com.cook.cancook.service;

import com.cook.cancook.dto.RatingDto;
import com.cook.cancook.mapper.RatingMapper;
import com.cook.cancook.model.RatingModel;
import com.cook.cancook.model.RecipesModel;
import com.cook.cancook.repository.RatingRepository;
import com.cook.cancook.repository.RecipesRepository;
import com.cook.cancook.repository.UserReposity;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class RatingService {

  @Autowired
  private RatingRepository ratingRepository;

  @Autowired
  private RatingMapper ratingMapper;

  @Autowired
  private RecipesRepository recipesRepository;

  @Autowired
  private UserReposity userReposity;

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
    validateRatingRequest(dto);

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

    RatingModel savedRating;
    try {
      savedRating = ratingRepository.save(rating);
    } catch (DataIntegrityViolationException ex) {
      // In case of race condition on unique (user_id, recipe_id), retry as update.
      Optional<RatingModel> concurrentRating =
          ratingRepository.findByRecipeIdAndUserId(dto.getRecipeId(), dto.getUserId());

      if (concurrentRating.isPresent()) {
        RatingModel updatedRating = concurrentRating.get();
        updatedRating.setRating(dto.getRating());
        updatedRating.setCreatedAt(LocalDateTime.now());
        savedRating = ratingRepository.save(updatedRating);
      } else {
        throw new ResponseStatusException(
            HttpStatus.BAD_REQUEST,
            "Unable to save rating. Verify recipeId/userId and payload keys.",
            ex);
      }
    }

    // Update recipe's average rating
    updateRecipeAverageRating(dto.getRecipeId());

    return ratingMapper.toDto(savedRating);
  }

  private void validateRatingRequest(RatingDto dto) {
    if (dto == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request body is required.");
    }

    if (dto.getRecipeId() == null) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "recipeId is required (or use recipe_id).");
    }

    if (dto.getUserId() == null) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "userId is required (or use user_id).");
    }

    if (dto.getRating() == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "rating is required.");
    }

    if (dto.getRating() < 0.0f || dto.getRating() > 5.0f) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "rating must be between 0 and 5.");
    }

    if (!recipesRepository.existsById(dto.getRecipeId())) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "recipeId does not exist: " + dto.getRecipeId());
    }

    if (!userReposity.existsById(dto.getUserId())) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "userId does not exist: " + dto.getUserId());
    }
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
