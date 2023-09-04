package com.kokio.entitymodule.domain.product.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemUpdateForm {

  private Long itemId;
  private String name;
  private Integer price;
  private Integer count;

}
