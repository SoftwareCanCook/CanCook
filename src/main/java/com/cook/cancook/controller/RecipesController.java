package com.cook.cancook.controller;

import com.cook.cancook.dto.RecipesDto;
import com.cook.cancook.service.RecipesService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/recipes")
@CrossOrigin(origins = "*")
public class RecipesController {

  @Autowired
  private RecipesService recipesService;

  @GetMapping
  public ResponseEntity<List<RecipesDto>> getAllRecipes() {
    List<RecipesDto> recipes = recipesService.getAllRecipes();
    return ResponseEntity.ok(recipes);
  }

  @GetMapping("/{id}")
  public ResponseEntity<RecipesDto> getRecipeById(@PathVariable Integer id) {
    return recipesService
        .getRecipeById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/user/{userId}")
  public ResponseEntity<List<RecipesDto>> getRecipesByUserId(@PathVariable Integer userId) {
    List<RecipesDto> recipes = recipesService.getRecipesByUserId(userId);
    return ResponseEntity.ok(recipes);
  }

  @GetMapping("/public")
  public ResponseEntity<List<RecipesDto>> getPublicRecipes() {
    List<RecipesDto> recipes = recipesService.getPublicRecipes();
    return ResponseEntity.ok(recipes);
  }

  @GetMapping("/search")
  public ResponseEntity<List<RecipesDto>> searchRecipesByName(@RequestParam String name) {
    List<RecipesDto> recipes = recipesService.searchRecipesByName(name);
    return ResponseEntity.ok(recipes);
  }

  @GetMapping("/rating/{minRating}")
  public ResponseEntity<List<RecipesDto>> getRecipesByMinRating(@PathVariable Float minRating) {
    List<RecipesDto> recipes = recipesService.getRecipesByMinRating(minRating);
    return ResponseEntity.ok(recipes);
  }

  @PostMapping
  public ResponseEntity<RecipesDto> createRecipe(@RequestBody RecipesDto dto) {
    RecipesDto createdRecipe = recipesService.createRecipe(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdRecipe);
  }

  @PutMapping("/{id}")
  public ResponseEntity<RecipesDto> updateRecipe(
      @PathVariable Integer id, @RequestBody RecipesDto dto) {
    return recipesService
        .updateRecipe(id, dto)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteRecipe(@PathVariable Integer id) {
    if (recipesService.deleteRecipe(id)) {
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.notFound().build();
  }
}
