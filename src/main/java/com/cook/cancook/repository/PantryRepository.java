package com.cook.cancook.repository;

import com.cook.cancook.model.PantryModel;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PantryRepository extends JpaRepository<PantryModel, Integer> {

  List<PantryModel> findByUserId(Integer userId);

  List<PantryModel> findByItemId(Integer itemId);

  List<PantryModel> findByUserIdAndItemId(Integer userId, Integer itemId);
}
