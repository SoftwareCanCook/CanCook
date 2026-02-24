package com.cook.cancook.controller;

import com.cook.cancook.dto.RatingDto;
import com.cook.cancook.service.RatingService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ratings")
public class RatingController {

  @Autowired
  private RatingService ratingService;

  @GetMapping
  public ResponseEntity<List<RatingDto>> getAllRatings() {
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
  public ResponseEntity<RatingDto> submitRating(@RequestBody RatingDto dto) {
    RatingDto submittedRating = ratingService.submitRating(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(submittedRating);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteRating(@PathVariable Integer id) {
    if (ratingService.deleteRating(id)) {
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.notFound().build();
  }
}
