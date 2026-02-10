package com.cook.cancook.controller;

import com.cook.cancook.dto.IngredientAvailabilityDto;
import com.cook.cancook.dto.RecipeIngredientsDto;
import com.cook.cancook.dto.RecipePantryCheckDto;
import com.cook.cancook.dto.ScaledRecipeDto;
import com.cook.cancook.service.RecipeIngredientsService;
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
@RequestMapping("/api/recipe-ingredients")
@CrossOrigin(origins = "*")
public class RecipeIngredientsController {

  @Autowired
  private RecipeIngredientsService recipeIngredientsService;

  @GetMapping
  public ResponseEntity<List<RecipeIngredientsDto>> getAllRecipeIngredients() {
    List<RecipeIngredientsDto> ingredients = recipeIngredientsService.getAllRecipeIngredients();
    return ResponseEntity.ok(ingredients);
  }

  @GetMapping("/{id}")
  public ResponseEntity<RecipeIngredientsDto> getRecipeIngredientById(@PathVariable Integer id) {
    return recipeIngredientsService
        .getRecipeIngredientById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/recipe/{recipeId}")
  public ResponseEntity<List<RecipeIngredientsDto>> getIngredientsByRecipeId(
      @PathVariable Integer recipeId) {
    List<RecipeIngredientsDto> ingredients =
        recipeIngredientsService.getIngredientsByRecipeId(recipeId);
    return ResponseEntity.ok(ingredients);
  }

  @GetMapping("/item/{itemId}")
  public ResponseEntity<List<RecipeIngredientsDto>> getIngredientsByItemId(
      @PathVariable Integer itemId) {
    List<RecipeIngredientsDto> ingredients =
        recipeIngredientsService.getIngredientsByItemId(itemId);
    return ResponseEntity.ok(ingredients);
  }

  @PostMapping
  public ResponseEntity<RecipeIngredientsDto> createRecipeIngredient(
      @RequestBody RecipeIngredientsDto dto) {
    RecipeIngredientsDto createdIngredient = recipeIngredientsService.createRecipeIngredient(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdIngredient);
  }

  @PostMapping("/bulk")
  public ResponseEntity<List<RecipeIngredientsDto>> createMultipleRecipeIngredients(
      @RequestBody List<RecipeIngredientsDto> dtos) {
    List<RecipeIngredientsDto> createdIngredients =
        recipeIngredientsService.createMultipleRecipeIngredients(dtos);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdIngredients);
  }

  @PutMapping("/{id}")
  public ResponseEntity<RecipeIngredientsDto> updateRecipeIngredient(
      @PathVariable Integer id, @RequestBody RecipeIngredientsDto dto) {
    return recipeIngredientsService
        .updateRecipeIngredient(id, dto)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteRecipeIngredient(@PathVariable Integer id) {
    if (recipeIngredientsService.deleteRecipeIngredient(id)) {
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.notFound().build();
  }

  @DeleteMapping("/recipe/{recipeId}")
  public ResponseEntity<Void> deleteIngredientsByRecipeId(@PathVariable Integer recipeId) {
    recipeIngredientsService.deleteIngredientsByRecipeId(recipeId);
    return ResponseEntity.noContent().build();
  }

  // Check if user can make a recipe with their pantry items
  @GetMapping("/recipe/{recipeId}/pantry-check")
  public ResponseEntity<RecipePantryCheckDto> checkPantryAvailability(
      @PathVariable Integer recipeId, @RequestParam Integer userId) {
    RecipePantryCheckDto pantryCheck =
        recipeIngredientsService.checkPantryAvailability(recipeId, userId);
    return ResponseEntity.ok(pantryCheck);
  }

  // Get list of missing ingredients for a recipe
  @GetMapping("/recipe/{recipeId}/missing-ingredients")
  public ResponseEntity<List<IngredientAvailabilityDto>> getMissingIngredients(
      @PathVariable Integer recipeId, @RequestParam Integer userId) {
    List<IngredientAvailabilityDto> missingIngredients =
        recipeIngredientsService.getMissingIngredients(recipeId, userId);
    return ResponseEntity.ok(missingIngredients);
  }

  // Scale recipe quantities (e.g., double or half a recipe)
  @GetMapping("/recipe/{recipeId}/scale")
  public ResponseEntity<ScaledRecipeDto> scaleRecipe(
      @PathVariable Integer recipeId, @RequestParam Double scaleFactor) {
    ScaledRecipeDto scaledRecipe = recipeIngredientsService.scaleRecipe(recipeId, scaleFactor);
    return ResponseEntity.ok(scaledRecipe);
  }
}
