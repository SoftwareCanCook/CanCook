package com.cook.cancook.service;

import com.cook.cancook.dto.StoreDto;
import com.cook.cancook.mapper.StoreMapper;
import com.cook.cancook.model.StoreModel;
import com.cook.cancook.repository.StoreRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class StoreService {

  @Autowired
  private StoreRepository storeRepository;

  @Autowired
  private StoreMapper storeMapper;

  public List<StoreDto> getAllStores() {
    return storeRepository.findAll().stream()
        .map(storeMapper::toDto)
        .collect(Collectors.toList());
  }

  public Optional<StoreDto> getStoreById(Integer id) {
    return storeRepository.findById(id).map(storeMapper::toDto);
  }

  public Optional<StoreDto> getStoreByName(String name) {
    return storeRepository.findByName(name).map(storeMapper::toDto);
  }

  public List<StoreDto> getStoresByCity(String city) {
    return storeRepository.findByCity(city).stream()
        .map(storeMapper::toDto)
        .collect(Collectors.toList());
  }

  public List<StoreDto> getStoresByState(String state) {
    return storeRepository.findByState(state).stream()
        .map(storeMapper::toDto)
        .collect(Collectors.toList());
  }

  public List<StoreDto> getStoresByCityAndState(String city, String state) {
    return storeRepository.findByCityAndState(city, state).stream()
        .map(storeMapper::toDto)
        .collect(Collectors.toList());
  }

  public StoreDto createStore(StoreDto dto) {
    StoreModel model = storeMapper.toModel(dto);
    if (model.getCreatedAt() == null) {
      model.setCreatedAt(LocalDateTime.now());
    }
    StoreModel savedModel = storeRepository.save(model);
    return storeMapper.toDto(savedModel);
  }

  public Optional<StoreDto> updateStore(Integer id, StoreDto dto) {
    return storeRepository
        .findById(id)
        .map(
            existing -> {
              if (dto.getName() != null) {
                existing.setName(dto.getName());
              }
              if (dto.getAddress() != null) {
                existing.setAddress(dto.getAddress());
              }
              if (dto.getCity() != null) {
                existing.setCity(dto.getCity());
              }
              if (dto.getState() != null) {
                existing.setState(dto.getState());
              }
              if (dto.getZipCode() != null) {
                existing.setZipCode(dto.getZipCode());
              }
              if (dto.getPhone() != null) {
                existing.setPhone(dto.getPhone());
              }
              StoreModel updated = storeRepository.save(existing);
              return storeMapper.toDto(updated);
            });
  }

  public boolean deleteStore(Integer id) {
    if (storeRepository.existsById(id)) {
      storeRepository.deleteById(id);
      return true;
    }
    return false;
  }
}
