package com.cook.cancook.mapper;

import com.cook.cancook.dto.PantryDto;
import com.cook.cancook.model.PantryModel;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PantryMapper {

  PantryMapper INSTANCE = Mappers.getMapper(PantryMapper.class);

  PantryDto toDto(PantryModel model);

  PantryModel toModel(PantryDto dto);

  List<PantryDto> toDtoList(List<PantryModel> models);

  List<PantryModel> toModelList(List<PantryDto> dtos);
}
