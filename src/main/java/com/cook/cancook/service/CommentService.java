package com.cook.cancook.service;

import com.cook.cancook.dto.CommentDto;
import com.cook.cancook.mapper.CommentMapper;
import com.cook.cancook.model.CommentModel;
import com.cook.cancook.model.UserModel;
import com.cook.cancook.repository.CommentRepository;
import com.cook.cancook.repository.UserReposity;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CommentService {

  @Autowired
  private CommentRepository commentRepository;

  @Autowired
  private CommentMapper commentMapper;

  @Autowired
  private UserReposity userRepository;

  public List<CommentDto> getAllComments() {
    List<CommentModel> comments = commentRepository.findAll();
    return commentMapper.toDtoList(comments);
  }

  public Optional<CommentDto> getCommentById(Integer id) {
    return commentRepository.findById(id).map(commentMapper::toDto);
  }

  public List<CommentDto> getCommentsByRecipeId(Integer recipeId) {
    List<CommentModel> comments = commentRepository.findByRecipeIdOrderByCreatedAtDesc(recipeId);
    List<CommentDto> commentDtos = commentMapper.toDtoList(comments);

    // Populate username for each comment
    for (CommentDto dto : commentDtos) {
      Optional<UserModel> user = userRepository.findById(dto.getUserId());
      user.ifPresent(u -> dto.setUsername(u.getUsername()));
    }

    return commentDtos;
  }

  public List<CommentDto> getCommentsByUserId(Integer userId) {
    List<CommentModel> comments = commentRepository.findByUserId(userId);
    return commentMapper.toDtoList(comments);
  }

  public CommentDto createComment(CommentDto dto) {
    CommentModel comment = commentMapper.toModel(dto);
    if (comment.getCreatedAt() == null) {
      comment.setCreatedAt(LocalDateTime.now());
    }
    CommentModel savedComment = commentRepository.save(comment);
    return commentMapper.toDto(savedComment);
  }

  public Optional<CommentDto> updateComment(Integer id, CommentDto dto) {
    return commentRepository.findById(id).map(existingComment -> {
      if (dto.getCommentText() != null) {
        existingComment.setCommentText(dto.getCommentText());
      }
      CommentModel updatedComment = commentRepository.save(existingComment);
      return commentMapper.toDto(updatedComment);
    });
  }

  public boolean deleteComment(Integer id) {
    if (commentRepository.existsById(id)) {
      commentRepository.deleteById(id);
      return true;
    }
    return false;
  }
}
