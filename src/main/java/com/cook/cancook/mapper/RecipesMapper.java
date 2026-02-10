package com.cook.cancook.mapper;

import com.cook.cancook.dto.RecipesDto;
import com.cook.cancook.model.RecipesModel;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RecipesMapper {

  RecipesMapper INSTANCE = Mappers.getMapper(RecipesMapper.class);

  RecipesDto toDto(RecipesModel model);

  RecipesModel toModel(RecipesDto dto);

  List<RecipesDto> toDtoList(List<RecipesModel> models);

  List<RecipesModel> toModelList(List<RecipesDto> dtos);
}
