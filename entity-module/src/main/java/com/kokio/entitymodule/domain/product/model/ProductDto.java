package com.kokio.entitymodule.domain.product.model;

import com.kokio.entitymodule.domain.product.entity.Product;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {

  private Long id;
  private Long seller_id;
  private String name;
  private String description;
  private Integer price;
  private Integer count;
  private List<ItemDto> items;

  public static ProductDto toDto(Product product) {
    return ProductDto.builder()
        .id(product.getId())
        .seller_id(product.getSellerId())
        .name(product.getName())
        .description(product.getDescription())
        .price(product.getPrice())
        .count(product.getCount())
        .items(product.getItems().stream().map(item -> ItemDto.toDto(item))
            .collect(Collectors.toList()))
        .build();
  }

  public static ProductDto toDtoWithOutItem(Product product) {
    return ProductDto.builder()
        .id(product.getId())
        .seller_id(product.getSellerId())
        .name(product.getName())
        .description(product.getDescription())
        .price(product.getPrice())
        .count(product.getCount())
        .build();
  }

}
