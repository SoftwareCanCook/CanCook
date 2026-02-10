package com.cook.cancook.controller;

import com.cook.cancook.dto.GroceryItemsDto;
import com.cook.cancook.service.GroceryItemsService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
@CrossOrigin(origins = "*")
public class GroceryItemsController {

  @Autowired
  private GroceryItemsService groceryItemsService;

  @GetMapping
  public ResponseEntity<List<GroceryItemsDto>> getAllGroceryItems() {
    List<GroceryItemsDto> items = groceryItemsService.getAllGroceryItems();
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
      @PathVariable Integer storeId) {
    List<GroceryItemsDto> items = groceryItemsService.getGroceryItemsByStoreId(storeId);
    return ResponseEntity.ok(items);
  }

  @GetMapping("/category/{category}")
  public ResponseEntity<List<GroceryItemsDto>> getGroceryItemsByCategory(
      @PathVariable String category) {
    List<GroceryItemsDto> items = groceryItemsService.getGroceryItemsByCategory(category);
    return ResponseEntity.ok(items);
  }

  @GetMapping("/search")
  public ResponseEntity<List<GroceryItemsDto>> searchGroceryItemsByName(@RequestParam String name) {
    List<GroceryItemsDto> items = groceryItemsService.searchGroceryItemsByName(name);
    return ResponseEntity.ok(items);
  }

  @GetMapping("/in-stock")
  public ResponseEntity<List<GroceryItemsDto>> getInStockItems(@RequestParam Integer minStock) {
    List<GroceryItemsDto> items = groceryItemsService.getInStockItems(minStock);
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
