package com.cook.cancook.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroceryItemsDto {

  private Integer id;
  private Integer storeId;
  private String name;
  private String category;
  private Integer quantity;
  private byte[] image;
  private Integer stock;
}
