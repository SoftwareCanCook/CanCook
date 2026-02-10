package com.cook.cancook.mapper;

import com.cook.cancook.dto.RecipeIngredientsDto;
import com.cook.cancook.model.RecipeIngredientsModel;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RecipeIngredientsMapper {

  RecipeIngredientsMapper INSTANCE = Mappers.getMapper(RecipeIngredientsMapper.class);

  RecipeIngredientsDto toDto(RecipeIngredientsModel model);

  RecipeIngredientsModel toModel(RecipeIngredientsDto dto);

  List<RecipeIngredientsDto> toDtoList(List<RecipeIngredientsModel> models);

  List<RecipeIngredientsModel> toModelList(List<RecipeIngredientsDto> dtos);
}
