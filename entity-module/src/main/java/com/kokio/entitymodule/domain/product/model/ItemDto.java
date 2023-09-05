package com.kokio.entitymodule.domain.product.model;


import com.kokio.entitymodule.domain.product.entity.Item;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {

  private Long id;
  private String name;
  private Integer price;
  private Integer count;

  public static ItemDto toDto(Item item) {
    return ItemDto.builder()
        .id(item.getId())
        .name(item.getName())
        .price(item.getPrice())
        .count(item.getCount())
        .build();
  }

}
