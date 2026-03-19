package com.cook.cancook.mapper;

import com.cook.cancook.dto.StoreDto;
import com.cook.cancook.model.StoreModel;
import org.springframework.stereotype.Component;

@Component
public class StoreMapper {

  public StoreDto toDto(StoreModel model) {
    if (model == null) {
      return null;
    }

    StoreDto dto = new StoreDto();
    dto.setId(model.getId());
    dto.setName(model.getName());
    dto.setAddress(model.getAddress());
    dto.setCity(model.getCity());
    dto.setState(model.getState());
    dto.setZipCode(model.getZipCode());
    dto.setPhone(model.getPhone());
    dto.setCreatedAt(model.getCreatedAt());

    return dto;
  }

  public StoreModel toModel(StoreDto dto) {
    if (dto == null) {
      return null;
    }

    StoreModel model = new StoreModel();
    model.setId(dto.getId());
    model.setName(dto.getName());
    model.setAddress(dto.getAddress());
    model.setCity(dto.getCity());
    model.setState(dto.getState());
    model.setZipCode(dto.getZipCode());
    model.setPhone(dto.getPhone());
    model.setCreatedAt(dto.getCreatedAt());

    return model;
  }
}
