package com.kokio.productapi.service;


import static com.kokio.commonmodule.exception.Code.ProductErrorCode.FAIL_FIND_ITEM;
import static com.kokio.commonmodule.exception.Code.ProductErrorCode.FAIL_FIND_PRODUCT;

import com.kokio.commonmodule.exception.ProductException;
import com.kokio.entitymodule.domain.product.model.AddProductCartForm;
import com.kokio.entitymodule.domain.product.repository.ProductRepository;
import com.kokio.entitymodule.domain.redis.Cart;
import com.kokio.entitymodule.domain.redis.Cart.Item;
import com.kokio.entitymodule.domain.redis.Cart.Product;
import com.kokio.entitymodule.domain.user.model.UserDto;
import com.kokio.productapi.client.RedisClient;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
@RequiredArgsConstructor
public class UserCartService {

  private final RedisClient redisClient;
  private final ProductRepository productRepository;
  private final SearchService searchService;

  public Cart getCart(Long userId) {
    Cart cart = redisClient.get(userId, Cart.class);
    return cart != null ? cart : new Cart(userId);
  }

  public void putCart(Long userId, Cart cart) {
    redisClient.put(userId, cart);
  }

  public Cart addCart(UserDto user, AddProductCartForm form) {
    //유저의 카트를 확인
    Cart cart = redisClient.get(user.getId(), Cart.class);
    //유저의 카트가 없으면, 새로 만들어 유저 등록
    if (ObjectUtils.isEmpty(cart)) {
      cart = new Cart();
      cart.setCustomerId(user.getId());
    }
    //form과 동일한 제품을 카트에서 찾기
    Optional<Product> optionalProduct = cart.getProducts().stream()
        .filter(product -> product.getId().equals(form.getId())).findFirst();

    //카트에 동일 제품이 있다면~
    if (optionalProduct.isPresent()) {
      Product redisProduct = optionalProduct.get();

      //카트 상품의 상세 상품
      Map<Long, Cart.Item> redisItemMap = redisProduct.getItems().stream()
          .collect(Collectors.toMap(item -> item.getId(), item -> item));

      //form의 세부 상품
      List<Item> formProductItems = form.getItems().stream().map(Item::cartToItem)
          .collect(Collectors.toList());

      //카트 상품과 집어 넣으려는 상품의 이름이 다름(이름이 변경됨)
      if (!redisProduct.getName().equals(form.getName())) {
        cart.addMessage(redisProduct.getName() + "의 상품명이 변경되었습니다. 확인 부탁드립니다.");
      }
      if (!Objects.equals(redisProduct.getPrice(), form.getPrice())) {
        cart.addMessage(redisProduct.getName() + "의 가격이 변경되었습니다. 확인 부탁드립니다.");
      }

      //상품의 상세 상품이 카트 상품의 상세 상품에 없다면 그대로 추가.
      for (Cart.Item item : formProductItems) {
        Cart.Item redisItem = redisItemMap.get(item.getId());
        if (redisItem == null) {
          redisProduct.getItems().add(item);
        } else {
          if (!redisItem.getName().equals(item.getName())) {
            cart.addMessage(
                redisProduct.getName() + "의 " + item.getName() + " 상품명이 변경되었습니다. 확인 부탁드립니다.");
          }
          if (redisItem.getPrice() != item.getPrice()) {
            cart.addMessage(
                redisProduct.getName() + "의 " + item.getName() + " 가격이 변경되었습니다. 확인 부탁드립니다.");
          }
          redisItem.setCount(redisItem.getCount() + item.getCount());

        }

      }
      redisProduct.setCount(redisProduct.getCount() + form.getCount());
      redisClient.put(user.getId(), cart);

    } else {  //카트에 동일 제품이 없지만, 판매 상품이 존재한다면!
      com.kokio.entitymodule.domain.product.entity.Product product = productRepository.findById(
          form.getId()).orElseThrow(() -> new ProductException(
          FAIL_FIND_PRODUCT
      ));


      if (!form.getPrice().equals(product.getPrice())) {

        cart.addMessage(form.getName() + "의 가격이 변경되었습니다. 확인 부탁드립니다.");
        form.setPrice(product.getPrice());
      }
      if (!form.getName().equals(product.getName())) {
        cart.addMessage(form.getName() + "의 상품명이 변경되었습니다. 확인 부탁드립니다.");
        form.setName(product.getName());
      }

      //form의 세부 상품
      List<Long> list = form.getItems().stream().map(AddProductCartForm.Item::getId).collect(
          Collectors.toList());


      //DB에 실제 세부 상품
      List<com.kokio.entitymodule.domain.product.entity.Item> items = searchService.getListByItemIds(
          list);
      //추가하고자 하는 세부 상품
      List<Item> formProductItems = form.getItems().stream().map(Item::cartToItem)
          .collect(Collectors.toList());

      for (com.kokio.entitymodule.domain.product.entity.Item item : items) {
        Item it = formProductItems.stream().filter(i -> i.getId().equals(item.getId()))
            .findFirst().orElseThrow(() -> new ProductException(FAIL_FIND_ITEM));
        if (!it.getPrice().equals(item.getPrice())){
          cart.addMessage("세부상품 "+it.getName()+"의 가격이 변경되었습니다. 확인 탁드립니다.");
          it.setPrice(item.getPrice());
        }
        if(!it.getName().equals(item.getName())){
          cart.addMessage("세부상품" + it.getName()+"의 제품명이 변경되었습니다. 확인 부탁드리빈다.");
          it.setName(item.getName());
        }
      }

      Cart.Product cartProduct = Product.cartToProduct(form);
      cart.getProducts().add(cartProduct);
    }
    redisClient.put(user.getId(), cart);
    return cart;
  }


}
