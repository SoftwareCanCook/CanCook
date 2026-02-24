package com.cook.cancook.repository;

import com.cook.cancook.model.GroceryItemsModel;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroceryItemsRepository extends JpaRepository<GroceryItemsModel, Integer> {

  List<GroceryItemsModel> findByStoreId(Integer storeId);

  List<GroceryItemsModel> findByStoreId(Integer storeId, Sort sort);

  List<GroceryItemsModel> findByCategory(String category);

  List<GroceryItemsModel> findByCategory(String category, Sort sort);

  List<GroceryItemsModel> findByNameContainingIgnoreCase(String name);

  List<GroceryItemsModel> findByNameContainingIgnoreCase(String name, Sort sort);

  List<GroceryItemsModel> findByStockGreaterThan(Integer stock);

  List<GroceryItemsModel> findByStockGreaterThan(Integer stock, Sort sort);

  List<GroceryItemsModel> findAll(Sort sort);
}
