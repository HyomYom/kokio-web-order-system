package com.kokio.entitymodule.domain.redis;


import com.kokio.entitymodule.domain.product.model.AddProductCartForm;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

@Getter
@NoArgsConstructor
@RedisHash("cart")
public class Cart {

  private Long customerId;

  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class Product {

    private Long id;
    private Long sellerId;
    private String name;
    private String description;
    private Integer price;
    private Integer count;
    private List<Item> items;

    public static Cart.Product cartToProduct(AddProductCartForm form) {
      return Product.builder()
          .id(form.getId())
          .sellerId(form.getSellerId())
          .name(form.getName())
          .description(form.getDescription())
          .price(form.getPrice())
          .count(form.getCount())
          .items(form.getItems().stream().map(Cart.Item::cartToItem).collect(Collectors.toList()))
          .build();
    }
  }

  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Item {

    private Long id;
    private Long sellerId;
    private String name;
    private Integer count;
    private Integer price;

    public static Cart.Item cartToItem(AddProductCartForm.Item form) {
      return Item.builder()
          .id(form.getId())
          .sellerId(form.getId())
          .name(form.getName())
          .count(form.getCount())
          .price(form.getPrice())
          .build();

    }
  }

}


