package com.cook.cancook.controller;

import com.cook.cancook.dto.RecipesDto;
import com.cook.cancook.service.RecipesService;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/recipes")
public class RecipesController {

  @Autowired
  private RecipesService recipesService;

  @GetMapping
  public ResponseEntity<List<RecipesDto>> getAllRecipes(
      @RequestParam(required = false) String sortBy,
      @RequestParam(required = false) String sortOrder) {
    List<RecipesDto> recipes;
    if (sortBy != null && !sortBy.isEmpty()) {
      recipes = recipesService.getAllRecipes(sortBy, sortOrder);
    } else {
      recipes = recipesService.getAllRecipes();
    }
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
  public ResponseEntity<List<RecipesDto>> getPublicRecipes(
      @RequestParam(required = false) String sortBy,
      @RequestParam(required = false) String sortOrder) {
    List<RecipesDto> recipes;
    if (sortBy != null && !sortBy.isEmpty()) {
      recipes = recipesService.getPublicRecipes(sortBy, sortOrder);
    } else {
      recipes = recipesService.getPublicRecipes();
    }
    return ResponseEntity.ok(recipes);
  }

  @GetMapping("/search")
  public ResponseEntity<List<RecipesDto>> searchRecipesByName(
      @RequestParam String name,
      @RequestParam(required = false) String sortBy,
      @RequestParam(required = false) String sortOrder) {
    List<RecipesDto> recipes;
    if (sortBy != null && !sortBy.isEmpty()) {
      recipes = recipesService.searchRecipesByName(name, sortBy, sortOrder);
    } else {
      recipes = recipesService.searchRecipesByName(name);
    }
    return ResponseEntity.ok(recipes);
  }

  @GetMapping("/rating/{minRating}")
  public ResponseEntity<List<RecipesDto>> getRecipesByMinRating(
      @PathVariable Float minRating,
      @RequestParam(required = false) String sortBy,
      @RequestParam(required = false) String sortOrder) {
    List<RecipesDto> recipes;
    if (sortBy != null && !sortBy.isEmpty()) {
      recipes = recipesService.getRecipesByMinRating(minRating, sortBy, sortOrder);
    } else {
      recipes = recipesService.getRecipesByMinRating(minRating);
    }
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

  @PostMapping("/{id}/image")
  public ResponseEntity<String> uploadRecipeImage(
      @PathVariable Integer id, @RequestParam("image") MultipartFile file) {
    try {
      byte[] imageBytes = file.getBytes();
      return recipesService
          .getRecipeById(id)
          .map(recipe -> {
            recipe.setImage(imageBytes);
            recipesService.updateRecipe(id, recipe);
            return ResponseEntity.ok("Image uploaded successfully");
          })
          .orElse(ResponseEntity.notFound().build());
    } catch (IOException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Failed to upload image: " + e.getMessage());
    }
  }

  @GetMapping("/{id}/image")
  public ResponseEntity<byte[]> getRecipeImage(@PathVariable Integer id) {
    return recipesService
        .getRecipeById(id)
        .filter(recipe -> recipe.getImage() != null)
        .map(
            recipe -> ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(recipe.getImage()))
        .orElse(ResponseEntity.notFound().build());
  }
}
