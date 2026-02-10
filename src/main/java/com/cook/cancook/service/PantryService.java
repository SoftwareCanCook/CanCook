package com.cook.cancook.service;

import com.cook.cancook.dto.PantryDto;
import com.cook.cancook.mapper.PantryMapper;
import com.cook.cancook.model.PantryModel;
import com.cook.cancook.repository.PantryRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PantryService {

  @Autowired
  private PantryRepository pantryRepository;

  @Autowired
  private PantryMapper pantryMapper;

  public List<PantryDto> getAllPantryItems() {
    List<PantryModel> items = pantryRepository.findAll();
    return pantryMapper.toDtoList(items);
  }

  public Optional<PantryDto> getPantryItemById(Integer id) {
    return pantryRepository.findById(id).map(pantryMapper::toDto);
  }

  public List<PantryDto> getPantryItemsByUserId(Integer userId) {
    List<PantryModel> items = pantryRepository.findByUserId(userId);
    return pantryMapper.toDtoList(items);
  }

  public List<PantryDto> getPantryItemsByItemId(Integer itemId) {
    List<PantryModel> items = pantryRepository.findByItemId(itemId);
    return pantryMapper.toDtoList(items);
  }

  public List<PantryDto> getPantryItemsByUserAndItem(Integer userId, Integer itemId) {
    List<PantryModel> items = pantryRepository.findByUserIdAndItemId(userId, itemId);
    return pantryMapper.toDtoList(items);
  }

  public PantryDto createPantryItem(PantryDto dto) {
    PantryModel item = pantryMapper.toModel(dto);
    PantryModel savedItem = pantryRepository.save(item);
    return pantryMapper.toDto(savedItem);
  }

  public Optional<PantryDto> updatePantryItem(Integer id, PantryDto dto) {
    return pantryRepository.findById(id).map(existingItem -> {
      if (dto.getUserId() != null) {
        existingItem.setUserId(dto.getUserId());
      }
      if (dto.getItemId() != null) {
        existingItem.setItemId(dto.getItemId());
      }
      if (dto.getQuantity() != null) {
        existingItem.setQuantity(dto.getQuantity());
      }
      PantryModel updatedItem = pantryRepository.save(existingItem);
      return pantryMapper.toDto(updatedItem);
    });
  }

  public boolean deletePantryItem(Integer id) {
    if (pantryRepository.existsById(id)) {
      pantryRepository.deleteById(id);
      return true;
    }
    return false;
  }
}
