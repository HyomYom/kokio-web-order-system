package com.kokio.entitymodule.domain.product.model;


import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class ProductUpdateForm {

  private Long productId;
  private String name;
  private String description;
  private Integer price;
  private Integer count;

  private List<ItemUpdateForm> items;

}
