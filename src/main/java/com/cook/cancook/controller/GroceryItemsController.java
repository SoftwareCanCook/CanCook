package com.cook.cancook.controller;

import com.cook.cancook.dto.GroceryItemsDto;
import com.cook.cancook.service.GroceryItemsService;
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
@RequestMapping("/api/grocery-items")
public class GroceryItemsController {

  @Autowired
  private GroceryItemsService groceryItemsService;

  @GetMapping
  public ResponseEntity<List<GroceryItemsDto>> getAllGroceryItems(
      @RequestParam(required = false) String sortBy,
      @RequestParam(required = false) String sortOrder) {
    List<GroceryItemsDto> items;
    if (sortBy != null && !sortBy.isEmpty()) {
      items = groceryItemsService.getAllGroceryItems(sortBy, sortOrder);
    } else {
      items = groceryItemsService.getAllGroceryItems();
    }
    return ResponseEntity.ok(items);
  }

  @GetMapping("/{id}")
  public ResponseEntity<GroceryItemsDto> getGroceryItemById(@PathVariable Integer id) {
    return groceryItemsService
        .getGroceryItemById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/store/{storeId}")
  public ResponseEntity<List<GroceryItemsDto>> getGroceryItemsByStoreId(
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

  @GetMapping("/category/{category}")
  public ResponseEntity<List<GroceryItemsDto>> getGroceryItemsByCategory(
      @PathVariable String category,
      @RequestParam(required = false) String sortBy,
      @RequestParam(required = false) String sortOrder) {
    List<GroceryItemsDto> items;
    if (sortBy != null && !sortBy.isEmpty()) {
      items = groceryItemsService.getGroceryItemsByCategory(category, sortBy, sortOrder);
    } else {
      items = groceryItemsService.getGroceryItemsByCategory(category);
    }
    return ResponseEntity.ok(items);
  }

  @GetMapping("/search")
  public ResponseEntity<List<GroceryItemsDto>> searchGroceryItemsByName(
      @RequestParam String name,
      @RequestParam(required = false) String sortBy,
      @RequestParam(required = false) String sortOrder) {
    List<GroceryItemsDto> items;
    if (sortBy != null && !sortBy.isEmpty()) {
      items = groceryItemsService.searchGroceryItemsByName(name, sortBy, sortOrder);
    } else {
      items = groceryItemsService.searchGroceryItemsByName(name);
    }
    return ResponseEntity.ok(items);
  }

  @GetMapping("/in-stock")
  public ResponseEntity<List<GroceryItemsDto>> getInStockItems(
      @RequestParam Integer minStock,
      @RequestParam(required = false) String sortBy,
      @RequestParam(required = false) String sortOrder) {
    List<GroceryItemsDto> items;
    if (sortBy != null && !sortBy.isEmpty()) {
      items = groceryItemsService.getInStockItems(minStock, sortBy, sortOrder);
    } else {
      items = groceryItemsService.getInStockItems(minStock);
    }
    return ResponseEntity.ok(items);
  }

  @PostMapping
  public ResponseEntity<GroceryItemsDto> createGroceryItem(@RequestBody GroceryItemsDto dto) {
    GroceryItemsDto createdItem = groceryItemsService.createGroceryItem(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdItem);
  }

  @PutMapping("/{id}")
  public ResponseEntity<GroceryItemsDto> updateGroceryItem(
      @PathVariable Integer id, @RequestBody GroceryItemsDto dto) {
    return groceryItemsService
        .updateGroceryItem(id, dto)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteGroceryItem(@PathVariable Integer id) {
    if (groceryItemsService.deleteGroceryItem(id)) {
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.notFound().build();
  }
}
