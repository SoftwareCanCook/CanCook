package com.cook.cancook.service;

import com.cook.cancook.dto.RecipesDto;
import com.cook.cancook.mapper.RecipesMapper;
import com.cook.cancook.model.RecipesModel;
import com.cook.cancook.repository.RecipesRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RecipesService {

  @Autowired
  private RecipesRepository recipesRepository;

  @Autowired
  private RecipesMapper recipesMapper;

  public List<RecipesDto> getAllRecipes() {
    List<RecipesModel> recipes = recipesRepository.findAll();
    return recipesMapper.toDtoList(recipes);
  }

  public Optional<RecipesDto> getRecipeById(Integer id) {
    return recipesRepository.findById(id).map(recipesMapper::toDto);
  }

  public List<RecipesDto> getRecipesByUserId(Integer userId) {
    List<RecipesModel> recipes = recipesRepository.findByUserId(userId);
    return recipesMapper.toDtoList(recipes);
  }

  public List<RecipesDto> getPublicRecipes() {
    List<RecipesModel> recipes = recipesRepository.findByIsPublic(true);
    return recipesMapper.toDtoList(recipes);
  }

  public List<RecipesDto> searchRecipesByName(String name) {
    List<RecipesModel> recipes = recipesRepository.findByNameContainingIgnoreCase(name);
    return recipesMapper.toDtoList(recipes);
  }

  public List<RecipesDto> getRecipesByMinRating(Float minRating) {
    List<RecipesModel> recipes = recipesRepository.findByRatingGreaterThanEqual(minRating);
    return recipesMapper.toDtoList(recipes);
  }

  public RecipesDto createRecipe(RecipesDto dto) {
    RecipesModel recipe = recipesMapper.toModel(dto);
    RecipesModel savedRecipe = recipesRepository.save(recipe);
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
      if (dto.getImageUrl() != null) {
        existingRecipe.setImageUrl(dto.getImageUrl());
      }
      if (dto.getIsPublic() != null) {
        existingRecipe.setIsPublic(dto.getIsPublic());
      }
      if (dto.getInstructionsAndTimers() != null) {
        existingRecipe.setInstructionsAndTimers(dto.getInstructionsAndTimers());
      }
      if (dto.getRating() != null) {
        existingRecipe.setRating(dto.getRating());
      }
      RecipesModel updatedRecipe = recipesRepository.save(existingRecipe);
      return recipesMapper.toDto(updatedRecipe);
    });
  }

  public boolean deleteRecipe(Integer id) {
    if (recipesRepository.existsById(id)) {
      recipesRepository.deleteById(id);
      return true;
    }
    return false;
  }
}
