package com.cook.cancook.service;

import com.cook.cancook.dto.GroceryItemsDto;
import com.cook.cancook.mapper.GroceryItemsMapper;
import com.cook.cancook.model.GroceryItemsModel;
import com.cook.cancook.repository.GroceryItemsRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class GroceryItemsService {

  @Autowired
  private GroceryItemsRepository groceryItemsRepository;

  @Autowired
  private GroceryItemsMapper groceryItemsMapper;

  public List<GroceryItemsDto> getAllGroceryItems() {
    List<GroceryItemsModel> items = groceryItemsRepository.findAll();
    return groceryItemsMapper.toDtoList(items);
  }

  public List<GroceryItemsDto> getAllGroceryItems(String sortBy, String sortOrder) {
    Sort sort = createSort(sortBy, sortOrder);
    List<GroceryItemsModel> items = groceryItemsRepository.findAll(sort);
    return groceryItemsMapper.toDtoList(items);
  }

  public Optional<GroceryItemsDto> getGroceryItemById(Integer id) {
    return groceryItemsRepository.findById(id).map(groceryItemsMapper::toDto);
  }

  public List<GroceryItemsDto> getGroceryItemsByStoreId(Integer storeId) {
    List<GroceryItemsModel> items = groceryItemsRepository.findByStoreId(storeId);
    return groceryItemsMapper.toDtoList(items);
  }

  public List<GroceryItemsDto> getGroceryItemsByStoreId(
      Integer storeId, String sortBy, String sortOrder) {
    Sort sort = createSort(sortBy, sortOrder);
    List<GroceryItemsModel> items = groceryItemsRepository.findByStoreId(storeId, sort);
    return groceryItemsMapper.toDtoList(items);
  }

  public List<GroceryItemsDto> getGroceryItemsByCategory(String category) {
    List<GroceryItemsModel> items = groceryItemsRepository.findByCategory(category);
    return groceryItemsMapper.toDtoList(items);
  }

  public List<GroceryItemsDto> getGroceryItemsByCategory(
      String category, String sortBy, String sortOrder) {
    Sort sort = createSort(sortBy, sortOrder);
    List<GroceryItemsModel> items = groceryItemsRepository.findByCategory(category, sort);
    return groceryItemsMapper.toDtoList(items);
  }

  public List<GroceryItemsDto> searchGroceryItemsByName(String name) {
    List<GroceryItemsModel> items = groceryItemsRepository.findByNameContainingIgnoreCase(name);
    return groceryItemsMapper.toDtoList(items);
  }

  public List<GroceryItemsDto> searchGroceryItemsByName(
      String name, String sortBy, String sortOrder) {
    Sort sort = createSort(sortBy, sortOrder);
    List<GroceryItemsModel> items =
        groceryItemsRepository.findByNameContainingIgnoreCase(name, sort);
    return groceryItemsMapper.toDtoList(items);
  }

  public List<GroceryItemsDto> getInStockItems(Integer minStock) {
    List<GroceryItemsModel> items = groceryItemsRepository.findByStockGreaterThan(minStock);
    return groceryItemsMapper.toDtoList(items);
  }

  public List<GroceryItemsDto> getInStockItems(Integer minStock, String sortBy, String sortOrder) {
    Sort sort = createSort(sortBy, sortOrder);
    List<GroceryItemsModel> items = groceryItemsRepository.findByStockGreaterThan(minStock, sort);
    return groceryItemsMapper.toDtoList(items);
  }

  public GroceryItemsDto createGroceryItem(GroceryItemsDto dto) {
    GroceryItemsModel item = groceryItemsMapper.toModel(dto);
    GroceryItemsModel savedItem = groceryItemsRepository.save(item);
    return groceryItemsMapper.toDto(savedItem);
  }

  public Optional<GroceryItemsDto> updateGroceryItem(Integer id, GroceryItemsDto dto) {
    return groceryItemsRepository.findById(id).map(existingItem -> {
      if (dto.getStoreId() != null) {
        existingItem.setStoreId(dto.getStoreId());
      }
      if (dto.getName() != null) {
        existingItem.setName(dto.getName());
      }
      if (dto.getCategory() != null) {
        existingItem.setCategory(dto.getCategory());
      }
      if (dto.getQuantity() != null) {
        existingItem.setQuantity(dto.getQuantity());
      }
      if (dto.getImage() != null) {
        existingItem.setImage(dto.getImage());
      }
      if (dto.getStock() != null) {
        existingItem.setStock(dto.getStock());
      }
      GroceryItemsModel updatedItem = groceryItemsRepository.save(existingItem);
      return groceryItemsMapper.toDto(updatedItem);
    });
  }

  public boolean deleteGroceryItem(Integer id) {
    if (groceryItemsRepository.existsById(id)) {
      groceryItemsRepository.deleteById(id);
      return true;
    }
    return false;
  }

  private Sort createSort(String sortBy, String sortOrder) {
    if (sortBy == null || sortBy.isEmpty()) {
      sortBy = "id"; // default
    }
    Sort.Direction direction =
        "desc".equalsIgnoreCase(sortOrder) ? Sort.Direction.DESC : Sort.Direction.ASC;
    return Sort.by(direction, sortBy);
  }
}
