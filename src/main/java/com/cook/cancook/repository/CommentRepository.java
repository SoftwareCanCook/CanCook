package com.cook.cancook.repository;

import com.cook.cancook.model.CommentModel;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<CommentModel, Integer> {

  List<CommentModel> findByRecipeId(Integer recipeId);

  List<CommentModel> findByUserId(Integer userId);

  List<CommentModel> findByRecipeIdOrderByCreatedAtDesc(Integer recipeId);
}
