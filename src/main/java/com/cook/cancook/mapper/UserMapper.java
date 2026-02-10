package com.cook.cancook.mapper;

import com.cook.cancook.dto.UserDto;
import com.cook.cancook.model.UserModel;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {

  UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

  UserDto toDto(UserModel model);

  UserModel toModel(UserDto dto);

  List<UserDto> toDtoList(List<UserModel> models);

  List<UserModel> toModelList(List<UserDto> dtos);
}
