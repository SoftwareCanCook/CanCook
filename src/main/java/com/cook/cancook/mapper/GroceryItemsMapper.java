package com.cook.cancook.mapper;

import com.cook.cancook.dto.GroceryItemsDto;
import com.cook.cancook.model.GroceryItemsModel;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface GroceryItemsMapper {

  GroceryItemsMapper INSTANCE = Mappers.getMapper(GroceryItemsMapper.class);

  GroceryItemsDto toDto(GroceryItemsModel model);

  GroceryItemsModel toModel(GroceryItemsDto dto);

  List<GroceryItemsDto> toDtoList(List<GroceryItemsModel> models);

  List<GroceryItemsModel> toModelList(List<GroceryItemsDto> dtos);
}
