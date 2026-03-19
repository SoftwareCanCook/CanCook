package com.cook.cancook.repository;

import com.cook.cancook.model.StoreModel;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends JpaRepository<StoreModel, Integer> {

  Optional<StoreModel> findByName(String name);

  List<StoreModel> findByCity(String city);

  List<StoreModel> findByState(String state);

  List<StoreModel> findByCityAndState(String city, String state);
}
