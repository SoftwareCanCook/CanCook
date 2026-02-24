package com.cook.cancook.mapper;

import com.cook.cancook.dto.CommentDto;
import com.cook.cancook.model.CommentModel;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {

  public CommentDto toDto(CommentModel model) {
    if (model == null) {
      return null;
    }
    CommentDto dto = new CommentDto();
    dto.setId(model.getId());
    dto.setRecipeId(model.getRecipeId());
    dto.setUserId(model.getUserId());
    dto.setCommentText(model.getCommentText());
    dto.setCreatedAt(model.getCreatedAt());
    return dto;
  }

  public CommentModel toModel(CommentDto dto) {
    if (dto == null) {
      return null;
    }
    CommentModel model = new CommentModel();
    model.setId(dto.getId());
    model.setRecipeId(dto.getRecipeId());
    model.setUserId(dto.getUserId());
    model.setCommentText(dto.getCommentText());
    model.setCreatedAt(dto.getCreatedAt());
    return model;
  }

  public List<CommentDto> toDtoList(List<CommentModel> models) {
    return models.stream().map(this::toDto).collect(Collectors.toList());
  }

  public List<CommentModel> toModelList(List<CommentDto> dtos) {
    return dtos.stream().map(this::toModel).collect(Collectors.toList());
  }
}
