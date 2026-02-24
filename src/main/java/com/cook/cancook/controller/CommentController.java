package com.cook.cancook.controller;

import com.cook.cancook.dto.CommentDto;
import com.cook.cancook.service.CommentService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

  @Autowired
  private CommentService commentService;

  @GetMapping
  public ResponseEntity<List<CommentDto>> getAllComments() {
    List<CommentDto> comments = commentService.getAllComments();
    return ResponseEntity.ok(comments);
  }

  @GetMapping("/{id}")
  public ResponseEntity<CommentDto> getCommentById(@PathVariable Integer id) {
    return commentService
        .getCommentById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/recipe/{recipeId}")
  public ResponseEntity<List<CommentDto>> getCommentsByRecipeId(@PathVariable Integer recipeId) {
    List<CommentDto> comments = commentService.getCommentsByRecipeId(recipeId);
    return ResponseEntity.ok(comments);
  }

  @GetMapping("/user/{userId}")
  public ResponseEntity<List<CommentDto>> getCommentsByUserId(@PathVariable Integer userId) {
    List<CommentDto> comments = commentService.getCommentsByUserId(userId);
    return ResponseEntity.ok(comments);
  }

  @PostMapping
  public ResponseEntity<CommentDto> createComment(@RequestBody CommentDto dto) {
    CommentDto createdComment = commentService.createComment(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
  }

  @PutMapping("/{id}")
  public ResponseEntity<CommentDto> updateComment(
      @PathVariable Integer id, @RequestBody CommentDto dto) {
    return commentService
        .updateComment(id, dto)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteComment(@PathVariable Integer id) {
    if (commentService.deleteComment(id)) {
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.notFound().build();
  }
}
