package com.cook.cancook.service;

import com.cook.cancook.dto.IngredientAvailabilityDto;
import com.cook.cancook.dto.RecipeIngredientsDto;
import com.cook.cancook.dto.RecipePantryCheckDto;
import com.cook.cancook.dto.ScaledIngredientDto;
import com.cook.cancook.dto.ScaledRecipeDto;
import com.cook.cancook.mapper.RecipeIngredientsMapper;
import com.cook.cancook.model.GroceryItemsModel;
import com.cook.cancook.model.PantryModel;
import com.cook.cancook.model.RecipeIngredientsModel;
import com.cook.cancook.repository.GroceryItemsRepository;
import com.cook.cancook.repository.PantryRepository;
import com.cook.cancook.repository.RecipeIngredientsRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RecipeIngredientsService {

  @Autowired
  private RecipeIngredientsRepository recipeIngredientsRepository;

  @Autowired
  private RecipeIngredientsMapper recipeIngredientsMapper;

  @Autowired
  private PantryRepository pantryRepository;

  @Autowired
  private GroceryItemsRepository groceryItemsRepository;

  public List<RecipeIngredientsDto> getAllRecipeIngredients() {
    List<RecipeIngredientsModel> ingredients = recipeIngredientsRepository.findAll();
    return recipeIngredientsMapper.toDtoList(ingredients);
  }

  public Optional<RecipeIngredientsDto> getRecipeIngredientById(Integer id) {
    return recipeIngredientsRepository.findById(id).map(recipeIngredientsMapper::toDto);
  }

  public List<RecipeIngredientsDto> getIngredientsByRecipeId(Integer recipeId) {
    List<RecipeIngredientsModel> ingredients = recipeIngredientsRepository.findByRecipeId(recipeId);
    return recipeIngredientsMapper.toDtoList(ingredients);
  }

  public List<RecipeIngredientsDto> getIngredientsByItemId(Integer itemId) {
    List<RecipeIngredientsModel> ingredients = recipeIngredientsRepository.findByItemId(itemId);
    return recipeIngredientsMapper.toDtoList(ingredients);
  }

  public RecipeIngredientsDto createRecipeIngredient(RecipeIngredientsDto dto) {
    RecipeIngredientsModel ingredient = recipeIngredientsMapper.toModel(dto);
    RecipeIngredientsModel savedIngredient = recipeIngredientsRepository.save(ingredient);
    return recipeIngredientsMapper.toDto(savedIngredient);
  }

  public List<RecipeIngredientsDto> createMultipleRecipeIngredients(
      List<RecipeIngredientsDto> dtos) {
    List<RecipeIngredientsModel> ingredients = recipeIngredientsMapper.toModelList(dtos);
    List<RecipeIngredientsModel> savedIngredients =
        recipeIngredientsRepository.saveAll(ingredients);
    return recipeIngredientsMapper.toDtoList(savedIngredients);
  }

  public Optional<RecipeIngredientsDto> updateRecipeIngredient(
      Integer id, RecipeIngredientsDto dto) {
    return recipeIngredientsRepository.findById(id).map(existingIngredient -> {
      if (dto.getRecipeId() != null) {
        existingIngredient.setRecipeId(dto.getRecipeId());
      }
      if (dto.getItemId() != null) {
        existingIngredient.setItemId(dto.getItemId());
      }
      if (dto.getQuantityNeeded() != null) {
        existingIngredient.setQuantityNeeded(dto.getQuantityNeeded());
      }
      if (dto.getMeasurementUnit() != null) {
        existingIngredient.setMeasurementUnit(dto.getMeasurementUnit());
      }
      RecipeIngredientsModel updatedIngredient =
          recipeIngredientsRepository.save(existingIngredient);
      return recipeIngredientsMapper.toDto(updatedIngredient);
    });
  }

  public boolean deleteRecipeIngredient(Integer id) {
    if (recipeIngredientsRepository.existsById(id)) {
      recipeIngredientsRepository.deleteById(id);
      return true;
    }
    return false;
  }

  public void deleteIngredientsByRecipeId(Integer recipeId) {
    recipeIngredientsRepository.deleteByRecipeId(recipeId);
  }

  // Check if user has ingredients in their pantry to make a recipe
  public RecipePantryCheckDto checkPantryAvailability(Integer recipeId, Integer userId) {
    List<RecipeIngredientsModel> recipeIngredients =
        recipeIngredientsRepository.findByRecipeId(recipeId);

    List<IngredientAvailabilityDto> ingredientStatus = new ArrayList<>();
    List<IngredientAvailabilityDto> missingIngredients = new ArrayList<>();
    boolean canMake = true;

    for (RecipeIngredientsModel ingredient : recipeIngredients) {
      List<PantryModel> pantryItems =
          pantryRepository.findByUserIdAndItemId(userId, ingredient.getItemId());

      int totalAvailable =
          pantryItems.stream().mapToInt(PantryModel::getQuantity).sum();

      Optional<GroceryItemsModel> groceryItem =
          groceryItemsRepository.findById(ingredient.getItemId());
      String itemName = groceryItem.map(GroceryItemsModel::getName).orElse("Unknown");

      int quantityNeeded = ingredient.getQuantityNeeded();
      boolean isAvailable = totalAvailable >= quantityNeeded;

      IngredientAvailabilityDto availabilityDto = new IngredientAvailabilityDto(
          ingredient.getItemId(),
          itemName,
          quantityNeeded,
          ingredient.getMeasurementUnit(),
          totalAvailable,
          isAvailable,
          isAvailable ? 0 : quantityNeeded - totalAvailable);

      ingredientStatus.add(availabilityDto);

      if (!isAvailable) {
        canMake = false;
        missingIngredients.add(availabilityDto);
      }
    }

    return new RecipePantryCheckDto(recipeId, canMake, ingredientStatus, missingIngredients);
  }

  // Get list of missing ingredients for a recipe
  public List<IngredientAvailabilityDto> getMissingIngredients(Integer recipeId, Integer userId) {
    RecipePantryCheckDto pantryCheck = checkPantryAvailability(recipeId, userId);
    return pantryCheck.getMissingIngredients();
  }

  // Scale recipe quantities by a factor (e.g., 2.0 for double, 0.5 for half)
  public ScaledRecipeDto scaleRecipe(Integer recipeId, Double scaleFactor) {
    List<RecipeIngredientsModel> recipeIngredients =
        recipeIngredientsRepository.findByRecipeId(recipeId);

    List<ScaledIngredientDto> scaledIngredients = recipeIngredients.stream()
        .map(ingredient -> {
          Optional<GroceryItemsModel> groceryItem =
              groceryItemsRepository.findById(ingredient.getItemId());
          String itemName = groceryItem.map(GroceryItemsModel::getName).orElse("Unknown");

          return new ScaledIngredientDto(
              ingredient.getItemId(),
              itemName,
              ingredient.getQuantityNeeded(),
              ingredient.getQuantityNeeded() * scaleFactor,
              ingredient.getMeasurementUnit());
        })
        .collect(Collectors.toList());

    return new ScaledRecipeDto(recipeId, scaleFactor, scaledIngredients);
  }
}
