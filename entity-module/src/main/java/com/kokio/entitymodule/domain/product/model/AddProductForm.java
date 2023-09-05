package com.kokio.entitymodule.domain.product.model;

import com.kokio.entitymodule.domain.product.entity.Product;
import com.kokio.entitymodule.domain.user.model.UserDto;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddProductForm {

  private String name;
  private String description;
  private Integer price;
  private Integer count;
  private List<AddItemForm> items;


  public static Product toProductWithItem(UserDto seller, AddProductForm form) {
    return Product.builder().sellerId(seller.getId()).name(form.name).description(form.description)
        .price(form.price)
        .count(form.count).items(
            form.items.stream().map(it -> AddItemForm.toItem(seller, it))
                .collect(Collectors.toList()))
        .build();
  }
}
