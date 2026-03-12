package com.cook.cancook.controller;

import com.cook.cancook.dto.GroceryItemsDto;
import com.cook.cancook.dto.StoreDto;
import com.cook.cancook.service.GroceryItemsService;
import com.cook.cancook.service.StoreService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stores")
public class StoreController {

  @Autowired
  private StoreService storeService;

  @Autowired
  private GroceryItemsService groceryItemsService;

  @GetMapping
  public ResponseEntity<List<StoreDto>> getAllStores(
      @RequestParam(required = false) String city,
      @RequestParam(required = false) String state) {
    List<StoreDto> stores;

    if (city != null && state != null) {
      stores = storeService.getStoresByCityAndState(city, state);
    } else if (city != null) {
      stores = storeService.getStoresByCity(city);
    } else if (state != null) {
      stores = storeService.getStoresByState(state);
    } else {
      stores = storeService.getAllStores();
    }

    return ResponseEntity.ok(stores);
  }

  @GetMapping("/{id}")
  public ResponseEntity<StoreDto> getStoreById(@PathVariable Integer id) {
    return storeService.getStoreById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/name/{name}")
  public ResponseEntity<StoreDto> getStoreByName(@PathVariable String name) {
    return storeService.getStoreByName(name).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<StoreDto> createStore(@RequestBody StoreDto dto) {
    StoreDto createdStore = storeService.createStore(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdStore);
  }

  @PutMapping("/{id}")
  public ResponseEntity<StoreDto> updateStore(
      @PathVariable Integer id, @RequestBody StoreDto dto) {
    return storeService
        .updateStore(id, dto)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteStore(@PathVariable Integer id) {
    if (storeService.deleteStore(id)) {
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.notFound().build();
  }

  // Grocery Items endpoints for stores
  @GetMapping("/{storeId}/items")
  public ResponseEntity<List<GroceryItemsDto>> getStoreItems(
      @PathVariable Integer storeId,
      @RequestParam(required = false) String sortBy,
      @RequestParam(required = false) String sortOrder) {
    List<GroceryItemsDto> items;
    if (sortBy != null && !sortBy.isEmpty()) {
      items = groceryItemsService.getGroceryItemsByStoreId(storeId, sortBy, sortOrder);
    } else {
      items = groceryItemsService.getGroceryItemsByStoreId(storeId);
    }
    return ResponseEntity.ok(items);
  }

  @PostMapping("/{storeId}/items")
  public ResponseEntity<GroceryItemsDto> addItemToStore(
      @PathVariable Integer storeId, @RequestBody GroceryItemsDto dto) {
    // Set the store ID from the path parameter
    dto.setStoreId(storeId);
    GroceryItemsDto createdItem = groceryItemsService.createGroceryItem(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdItem);
  }

  @PutMapping("/{storeId}/items/{itemId}")
  public ResponseEntity<GroceryItemsDto> updateStoreItem(
      @PathVariable Integer storeId,
      @PathVariable Integer itemId,
      @RequestBody GroceryItemsDto dto) {
    // Verify the item exists and belongs to the specified store
    var existingItem = groceryItemsService.getGroceryItemById(itemId);
    if (existingItem.isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    if (!existingItem.get().getStoreId().equals(storeId)) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
    return groceryItemsService
        .updateGroceryItem(itemId, dto)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{storeId}/items/{itemId}")
  public ResponseEntity<Void> deleteStoreItem(
      @PathVariable Integer storeId, @PathVariable Integer itemId) {
    // Verify the item belongs to the specified store before deleting
    var existingItem = groceryItemsService.getGroceryItemById(itemId);
    if (existingItem.isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    if (!existingItem.get().getStoreId().equals(storeId)) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
    if (groceryItemsService.deleteGroceryItem(itemId)) {
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.notFound().build();
  }
}
