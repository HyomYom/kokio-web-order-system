package com.kokio.entitymodule.domain.product.model;


import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddProductCartForm {

  private Long id;
  private Long sellerId;
  private String name;
  private String description;
  private Integer price;
  private Integer count;
  private List<Item> items;

  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Item {

    private Long id;
    private String name;
    private Integer count;
    private Integer price;
  }

}
