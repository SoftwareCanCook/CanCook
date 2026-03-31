package com.cook.cancook.controller;

import com.cook.cancook.dto.RatingDto;
import com.cook.cancook.service.RatingService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/ratings")
public class RatingController {

  @Autowired
  private RatingService ratingService;

  @GetMapping
  public ResponseEntity<List<RatingDto>> getAllRatings(
      @org.springframework.web.bind.annotation.RequestParam(required = false) Integer recipeId,
      @org.springframework.web.bind.annotation.RequestParam(required = false) String recipe_id,
      @org.springframework.web.bind.annotation.RequestParam(required = false) Integer userId,
      @org.springframework.web.bind.annotation.RequestParam(required = false) String user_id) {
    // Handle both camelCase and snake_case query parameters
    Integer effectiveRecipeId =
        recipeId != null ? recipeId : (recipe_id != null ? Integer.parseInt(recipe_id) : null);
    Integer effectiveUserId =
        userId != null ? userId : (user_id != null ? Integer.parseInt(user_id) : null);

    if (effectiveRecipeId != null) {
      return ResponseEntity.ok(ratingService.getRatingsByRecipeId(effectiveRecipeId));
    }
    if (effectiveUserId != null) {
      return ResponseEntity.ok(ratingService.getRatingsByUserId(effectiveUserId));
    }

    List<RatingDto> ratings = ratingService.getAllRatings();
    return ResponseEntity.ok(ratings);
  }

  @GetMapping("/{id}")
  public ResponseEntity<RatingDto> getRatingById(@PathVariable Integer id) {
    return ratingService
        .getRatingById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/recipe/{recipeId}")
  public ResponseEntity<List<RatingDto>> getRatingsByRecipeId(@PathVariable Integer recipeId) {
    List<RatingDto> ratings = ratingService.getRatingsByRecipeId(recipeId);
    return ResponseEntity.ok(ratings);
  }

  @GetMapping("/recipe/{recipeId}/average")
  public ResponseEntity<Float> getAverageRating(@PathVariable Integer recipeId) {
    Float avgRating = ratingService.getAverageRating(recipeId);
    if (avgRating != null) {
      return ResponseEntity.ok(avgRating);
    }
    return ResponseEntity.ok(0.0f);
  }

  @GetMapping("/user/{userId}")
  public ResponseEntity<List<RatingDto>> getRatingsByUserId(@PathVariable Integer userId) {
    List<RatingDto> ratings = ratingService.getRatingsByUserId(userId);
    return ResponseEntity.ok(ratings);
  }

  @PostMapping
  public ResponseEntity<?> submitRating(@RequestBody RatingDto dto) {
    try {
      RatingDto submittedRating = ratingService.submitRating(dto);
      return ResponseEntity.status(HttpStatus.CREATED).body(submittedRating);
    } catch (ResponseStatusException ex) {
      return ResponseEntity.status(ex.getStatusCode())
          .body(
              Map.of("error", ex.getReason() != null ? ex.getReason() : "Rating request failed."));
    } catch (DataAccessException ex) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(Map.of("error", "Database error while saving rating."));
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteRating(@PathVariable Integer id) {
    if (ratingService.deleteRating(id)) {
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.notFound().build();
  }
}
