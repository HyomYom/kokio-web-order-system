package com.kokio.entitymodule.domain.redis;


import com.kokio.entitymodule.domain.product.model.AddProductCartForm;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

@Getter
@Setter
@NoArgsConstructor
@RedisHash("cart")
public class Cart {


  @Id
  private Long customerId;
  private List<Product> products = new ArrayList<>();
  private List<String> messages = new ArrayList<>();

  public void addMessage(String message) {
    this.messages.add(message);
  }

  public Cart(Long customerId) {
    this.customerId = customerId;
  }

  @Getter
  @Setter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class Product {

    private Long id;
    private Long sellerId;
    private String name;
    private Integer price;
    private Integer count;
    private List<Item> items;

    public static Cart.Product cartToProduct(AddProductCartForm form) {
      return Product.builder()
          .id(form.getId())
          .sellerId(form.getSellerId())
          .name(form.getName())
          .price(form.getPrice())
          .count(form.getCount())
          .items(form.getItems().stream().map(Cart.Item::cartToItem).collect(Collectors.toList()))
          .build();
    }
  }

  @Getter
  @Setter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Item {

    private Long id;
    private String name;
    private Integer count;
    private Integer price;

    public static Cart.Item cartToItem(AddProductCartForm.Item form) {
      return Item.builder()
          .id(form.getId())
          .name(form.getName())
          .count(form.getCount())
          .price(form.getPrice())
          .build();

    }
  }

}


