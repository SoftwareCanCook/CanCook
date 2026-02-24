package com.cook.cancook.mapper;

import com.cook.cancook.dto.RatingDto;
import com.cook.cancook.model.RatingModel;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class RatingMapper {

  public RatingDto toDto(RatingModel model) {
    if (model == null) {
      return null;
    }
    RatingDto dto = new RatingDto();
    dto.setId(model.getId());
    dto.setRecipeId(model.getRecipeId());
    dto.setUserId(model.getUserId());
    dto.setRating(model.getRating());
    dto.setCreatedAt(model.getCreatedAt());
    return dto;
  }

  public RatingModel toModel(RatingDto dto) {
    if (dto == null) {
      return null;
    }
    RatingModel model = new RatingModel();
    model.setId(dto.getId());
    model.setRecipeId(dto.getRecipeId());
    model.setUserId(dto.getUserId());
    model.setRating(dto.getRating());
    model.setCreatedAt(dto.getCreatedAt());
    return model;
  }

  public List<RatingDto> toDtoList(List<RatingModel> models) {
    return models.stream().map(this::toDto).collect(Collectors.toList());
  }

  public List<RatingModel> toModelList(List<RatingDto> dtos) {
    return dtos.stream().map(this::toModel).collect(Collectors.toList());
  }
}
