package com.kokio.entitymodule.domain.product.model;


import com.kokio.entitymodule.domain.product.entity.Item;
import com.kokio.entitymodule.domain.user.model.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddItemForm {

  private String name;
  private Long productId;
  private Integer price;
  private Integer count;

  public static Item toItem(UserDto seller, AddItemForm form) {
    return Item.builder()
        .sellerId(seller.getId())
        .name(form.getName())
        .price(form.getPrice())
        .count(form.getCount())
        .build();
  }

}
