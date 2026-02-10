package com.cook.cancook.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
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
  private Integer storeId;

  @Column(length = 355)
  private String name;

  @Column(length = 355)
  private String category;

  @Column
  private Integer quantity;

  @Lob
  @Column(columnDefinition = "LONGBLOB")
  private byte[] image;

  @Column
  private Integer stock;
}
