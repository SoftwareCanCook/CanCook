package com.cook.cancook.controller;

import com.cook.cancook.dto.PantryDto;
import com.cook.cancook.service.PantryService;
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
@RequestMapping("/api/pantry")
public class PantryController {

  @Autowired
  private PantryService pantryService;

  @GetMapping
  public ResponseEntity<List<PantryDto>> getAllPantryItems() {
    List<PantryDto> items = pantryService.getAllPantryItems();
    return ResponseEntity.ok(items);
  }

  @GetMapping("/{id}")
  public ResponseEntity<PantryDto> getPantryItemById(@PathVariable Integer id) {
    return pantryService
        .getPantryItemById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/user/{userId}")
  public ResponseEntity<List<PantryDto>> getPantryItemsByUserId(@PathVariable Integer userId) {
    List<PantryDto> items = pantryService.getPantryItemsByUserId(userId);
    return ResponseEntity.ok(items);
  }

  @GetMapping("/item/{itemId}")
  public ResponseEntity<List<PantryDto>> getPantryItemsByItemId(@PathVariable Integer itemId) {
    List<PantryDto> items = pantryService.getPantryItemsByItemId(itemId);
    return ResponseEntity.ok(items);
  }

  @GetMapping("/user/{userId}/item/{itemId}")
  public ResponseEntity<List<PantryDto>> getPantryItemsByUserAndItem(
      @PathVariable Integer userId, @PathVariable Integer itemId) {
    List<PantryDto> items = pantryService.getPantryItemsByUserAndItem(userId, itemId);
    return ResponseEntity.ok(items);
  }

  @PostMapping
  public ResponseEntity<PantryDto> createPantryItem(@RequestBody PantryDto dto) {
    PantryDto createdItem = pantryService.createPantryItem(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdItem);
  }

  @PutMapping("/{id}")
  public ResponseEntity<PantryDto> updatePantryItem(
      @PathVariable Integer id, @RequestBody PantryDto dto) {
    return pantryService
        .updatePantryItem(id, dto)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deletePantryItem(@PathVariable Integer id) {
    if (pantryService.deletePantryItem(id)) {
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.notFound().build();
  }
}
