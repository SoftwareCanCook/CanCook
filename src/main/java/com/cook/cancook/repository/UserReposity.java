package com.cook.cancook.repository;

import com.cook.cancook.model.UserModel;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserReposity extends JpaRepository<UserModel, Integer> {

  Optional<UserModel> findByUsername(String username);

  boolean existsByUsername(String username);

  Optional<UserModel> findByPantryId(Integer pantryId);
}
