package com.cook.cancook.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "GroceryItems")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroceryItemsModel {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "store_id")
  @JsonProperty("store_id")
  private Integer storeId;

  @Column(length = 355)
  private String name;

  @Column(length = 355)
  private String category;

  @Column(length = 50)
  private String unit;

  @Column(length = 1000)
  private String image;

  @Column
  private Integer stock;
}
