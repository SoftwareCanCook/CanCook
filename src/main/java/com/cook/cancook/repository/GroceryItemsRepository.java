package com.cook.cancook.repository;

import com.cook.cancook.model.GroceryItemsModel;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroceryItemsRepository extends JpaRepository<GroceryItemsModel, Integer> {

  List<GroceryItemsModel> findByStoreId(Integer storeId);

  List<GroceryItemsModel> findByCategory(String category);

  List<GroceryItemsModel> findByNameContainingIgnoreCase(String name);

  List<GroceryItemsModel> findByStockGreaterThan(Integer stock);
}
