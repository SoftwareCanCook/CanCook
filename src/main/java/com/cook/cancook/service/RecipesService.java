package com.cook.cancook.service;

import com.cook.cancook.dto.RecipeDetailDto;
import com.cook.cancook.dto.RecipeIngredientInputDto;
import com.cook.cancook.dto.RecipeIngredientsDto;
import com.cook.cancook.dto.RecipesDto;
import com.cook.cancook.mapper.RecipesMapper;
import com.cook.cancook.model.GroceryItemsModel;
import com.cook.cancook.model.RecipesModel;
import com.cook.cancook.repository.GroceryItemsRepository;
import com.cook.cancook.repository.RecipesRepository;
import com.cook.cancook.util.ImageUrlResolver;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class RecipesService {

  @Autowired
  private RecipesRepository recipesRepository;

  @Autowired
  private RecipesMapper recipesMapper;

  @Autowired
  private RecipeIngredientsService recipeIngredientsService;

  @Autowired
  private GroceryItemsRepository groceryItemsRepository;

  public List<RecipesDto> getAllRecipes() {
    List<RecipesModel> recipes = recipesRepository.findAll();
    applyDefaultImages(recipes);
    return recipesMapper.toDtoList(recipes);
  }

  public List<RecipesDto> getAllRecipes(String sortBy, String sortOrder) {
    Sort sort = createSort(sortBy, sortOrder);
    List<RecipesModel> recipes = recipesRepository.findAll(sort);
    applyDefaultImages(recipes);
    return recipesMapper.toDtoList(recipes);
  }

  public Optional<RecipesDto> getRecipeById(Integer id) {
    return recipesRepository.findById(id).map(recipe -> {
      applyDefaultImage(recipe);
      return recipesMapper.toDto(recipe);
    });
  }

  public Optional<RecipeDetailDto> getRecipeDetailById(Integer id) {
    return recipesRepository.findById(id).map(recipe -> {
      applyDefaultImage(recipe);
      RecipesDto recipeDto = recipesMapper.toDto(recipe);
      List<RecipeIngredientsDto> ingredients =
          recipeIngredientsService.getIngredientsByRecipeId(id);

      return new RecipeDetailDto(
          recipeDto.getId(),
          recipeDto.getUserId(),
          recipeDto.getName(),
          recipeDto.getImage(),
          recipeDto.getIsPublic(),
          recipeDto.getInstructions(),
          recipeDto.getTimers(),
          recipeDto.getRating(),
          ingredients);
    });
  }

  public List<RecipesDto> getRecipesByUserId(Integer userId) {
    List<RecipesModel> recipes = recipesRepository.findByUserId(userId);
    applyDefaultImages(recipes);
    return recipesMapper.toDtoList(recipes);
  }

  public List<RecipesDto> getPublicRecipes() {
    List<RecipesModel> recipes = recipesRepository.findByIsPublic(true);
    applyDefaultImages(recipes);
    return recipesMapper.toDtoList(recipes);
  }

  public List<RecipesDto> getPublicRecipes(String sortBy, String sortOrder) {
    Sort sort = createSort(sortBy, sortOrder);
    List<RecipesModel> recipes = recipesRepository.findByIsPublic(true, sort);
    applyDefaultImages(recipes);
    return recipesMapper.toDtoList(recipes);
  }

  public List<RecipesDto> searchRecipesByName(String name) {
    List<RecipesModel> recipes = recipesRepository.findByNameContainingIgnoreCase(name);
    applyDefaultImages(recipes);
    return recipesMapper.toDtoList(recipes);
  }

  public List<RecipesDto> searchRecipesByName(String name, String sortBy, String sortOrder) {
    Sort sort = createSort(sortBy, sortOrder);
    List<RecipesModel> recipes = recipesRepository.findByNameContainingIgnoreCase(name, sort);
    applyDefaultImages(recipes);
    return recipesMapper.toDtoList(recipes);
  }

  public List<RecipesDto> getRecipesByMinRating(Float minRating) {
    List<RecipesModel> recipes = recipesRepository.findByRatingGreaterThanEqual(minRating);
    applyDefaultImages(recipes);
    return recipesMapper.toDtoList(recipes);
  }

  public List<RecipesDto> getRecipesByMinRating(Float minRating, String sortBy, String sortOrder) {
    Sort sort = createSort(sortBy, sortOrder);
    List<RecipesModel> recipes = recipesRepository.findByRatingGreaterThanEqual(minRating, sort);
    applyDefaultImages(recipes);
    return recipesMapper.toDtoList(recipes);
  }

  public RecipesDto createRecipe(RecipesDto dto) {
    RecipesModel recipe = recipesMapper.toModel(dto);
    recipe.setImage(ImageUrlResolver.resolveImageUrl(recipe.getImage(), recipe.getName()));
    RecipesModel savedRecipe = recipesRepository.save(recipe);
    persistRecipeIngredients(savedRecipe.getId(), dto.getIngredients(), false);
    return recipesMapper.toDto(savedRecipe);
  }

  public Optional<RecipesDto> updateRecipe(Integer id, RecipesDto dto) {
    return recipesRepository.findById(id).map(existingRecipe -> {
      if (dto.getUserId() != null) {
        existingRecipe.setUserId(dto.getUserId());
      }
      if (dto.getName() != null) {
        existingRecipe.setName(dto.getName());
      }
      if (dto.getImage() != null) {
        existingRecipe.setImage(dto.getImage());
      }
      if (dto.getIsPublic() != null) {
        existingRecipe.setIsPublic(dto.getIsPublic());
      }
      if (dto.getInstructions() != null) {
        existingRecipe.setInstructions(dto.getInstructions());
      }
      if (dto.getTimers() != null) {
        existingRecipe.setTimers(dto.getTimers());
      }
      if (dto.getRating() != null) {
        existingRecipe.setRating(dto.getRating());
      }
      existingRecipe.setImage(
          ImageUrlResolver.resolveImageUrl(existingRecipe.getImage(), existingRecipe.getName()));
      RecipesModel updatedRecipe = recipesRepository.save(existingRecipe);
      persistRecipeIngredients(updatedRecipe.getId(), dto.getIngredients(), true);
      return recipesMapper.toDto(updatedRecipe);
    });
  }

  private void persistRecipeIngredients(
      Integer recipeId,
      List<RecipeIngredientInputDto> ingredientInputs,
      boolean replaceExisting) {
    if (ingredientInputs == null) {
      return;
    }

    if (replaceExisting) {
      recipeIngredientsService.deleteIngredientsByRecipeId(recipeId);
    }

    List<RecipeIngredientsDto> ingredientDtos = new ArrayList<>();
    for (RecipeIngredientInputDto input : ingredientInputs) {
      Integer itemId = resolveItemId(input);
      if (itemId == null) {
        continue;
      }

      Integer quantityNeeded = input.getQuantityNeeded() != null
          ? input.getQuantityNeeded()
          : (input.getQuantity() != null ? input.getQuantity() : 1);
      String measurementUnit = input.getMeasurementUnit() != null
          ? input.getMeasurementUnit()
          : (input.getUnit() != null ? input.getUnit() : "unit");

      ingredientDtos.add(
          new RecipeIngredientsDto(
              null,
              recipeId,
              itemId,
              quantityNeeded,
              measurementUnit));
    }

    if (!ingredientDtos.isEmpty()) {
      recipeIngredientsService.createMultipleRecipeIngredients(ingredientDtos);
    }
  }

  private Integer resolveItemId(RecipeIngredientInputDto input) {
    if (input == null) {
      return null;
    }

    if (input.getItemId() != null) {
      return input.getItemId();
    }

    String ingredientName = input.getName() != null ? input.getName() : input.getIngredientName();
    if (ingredientName == null || ingredientName.trim().isEmpty()) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Each ingredient must include itemId or name");
    }

    List<GroceryItemsModel> matches =
        groceryItemsRepository.findByNameContainingIgnoreCase(ingredientName.trim());
    if (matches.isEmpty()) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Ingredient not found in GroceryItems: " + ingredientName);
    }

    String target = ingredientName.trim();
    return matches.stream()
        .filter(item -> item.getName() != null && item.getName().equalsIgnoreCase(target))
        .findFirst()
        .orElse(matches.get(0))
        .getId();
  }

  public boolean deleteRecipe(Integer id) {
    if (recipesRepository.existsById(id)) {
      recipesRepository.deleteById(id);
      return true;
    }
    return false;
  }

  private Sort createSort(String sortBy, String sortOrder) {
    if (sortBy == null || sortBy.isEmpty()) {
      sortBy = "id"; // default
    }
    Sort.Direction direction =
        "desc".equalsIgnoreCase(sortOrder) ? Sort.Direction.DESC : Sort.Direction.ASC;
    return Sort.by(direction, sortBy);
  }

  private void applyDefaultImages(List<RecipesModel> recipes) {
    for (RecipesModel recipe : recipes) {
      applyDefaultImage(recipe);
    }
  }

  private void applyDefaultImage(RecipesModel recipe) {
    if (recipe == null) {
      return;
    }
    recipe.setImage(ImageUrlResolver.resolveImageUrl(recipe.getImage(), recipe.getName()));
  }
}
