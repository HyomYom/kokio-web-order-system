package com.kokio.productapi.application;


import static com.kokio.commonmodule.exception.Code.CartErrorCode.FAIL_UPDATE_CART;
import static com.kokio.commonmodule.exception.Code.ProductErrorCode.FAIL_FIND_ITEM;
import static com.kokio.commonmodule.exception.Code.ProductErrorCode.FAIL_FIND_PRODUCT;
import static com.kokio.commonmodule.exception.Code.ProductErrorCode.PRODUCT_ITEM_COUNT_NOT_ENOUGH;

import com.kokio.commonmodule.exception.CartException;
import com.kokio.commonmodule.exception.ProductException;
import com.kokio.entitymodule.domain.product.entity.Product;
import com.kokio.entitymodule.domain.product.model.AddProductCartForm;
import com.kokio.entitymodule.domain.redis.Cart;
import com.kokio.entitymodule.domain.redis.Cart.Item;
import com.kokio.entitymodule.domain.user.model.UserDto;
import com.kokio.productapi.service.SearchService;
import com.kokio.productapi.service.UserCartService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
@RequiredArgsConstructor
public class UserCartApplication {

  private final UserCartService userCartService;
  private final SearchService searchService;


  public Cart addCart(UserDto user, AddProductCartForm form) {
    Product product = searchService.getByProductId(form.getId());
    if (ObjectUtils.isEmpty(product)) {
      throw new ProductException(FAIL_FIND_PRODUCT);
    }
    if (!ObjectUtils.isEmpty(form.getItems())) {
      List<com.kokio.entitymodule.domain.product.entity.Item> items = product.getItems();
      if (ObjectUtils.isEmpty(items)) {
        throw new ProductException(FAIL_FIND_ITEM);
      }
    }
    Cart cart = refreshCart(userCartService.getCart(user.getId()));
    if (!cart.getProducts().isEmpty() && !addAble(cart, product, form)) {
      throw new ProductException(PRODUCT_ITEM_COUNT_NOT_ENOUGH);
    }
    userCartService.addCart(user, form);
    return getCart(user);

  }

  public Cart getCart(UserDto user) {
    Cart cart = refreshCart(userCartService.getCart(user.getId()));
    Cart returnCart = new Cart();
    returnCart.setCustomerId(cart.getCustomerId());
    returnCart.setProducts(cart.getProducts());
    returnCart.setMessages(cart.getMessages());
    cart.setMessages(new ArrayList<>());
    userCartService.putCart(user.getId(), cart);
    return returnCart;

  }

  public Cart updateCart(UserDto user, Cart cart) {
    if (ObjectUtils.isEmpty(getCart(user).getProducts())) {
      throw new CartException(FAIL_UPDATE_CART);
    }
    if (!ObjectUtils.isEmpty(cart.getMessages())) {
      cart.setMessages(new ArrayList<>());
    }
    userCartService.putCart(user.getId(), cart);
    return getCart(user);
  }

  protected Cart refreshCart(Cart cart) {
    // 1. 상품이나 상품의 아이템의 정보, 가격 수량이 변경되었는지 체크하고
    // 상품이 없다면 카트에서 제거
    // 그에 맞는 알람을 제공한다.
    // 2. 상품의 수량, 가격을 우리의 임으대로 변경한다.(가능한 최대 개수)

    //카트 products의 id리스트로 넘기고, 정보로 넘어온 products를 id/Object Map 형태로 매핑!
    Map<Long, Product> productMap = searchService.getListByProductIds(
            cart.getProducts().stream().map(Cart.Product::getId).collect(
                Collectors.toList())).stream()
        .collect(Collectors.toMap(Product::getId, product -> product));

    for (int i = 0; i < cart.getProducts().size(); i++) {
      List<String> productTmpMessages = new ArrayList<>();
      Map<Boolean, String> statusMessages = new HashMap<>();

      Cart.Product cartProduct = cart.getProducts().get(i);
      Product p = productMap.get(cartProduct.getId());
      if (ObjectUtils.isEmpty(p)) {
        cart.getProducts().remove(cartProduct);
        i--;
        cart.addMessage(cartProduct.getName() + " 이(가) 더이상 존재하지 않습니다.");
        continue;
      }

//      boolean isNameChanged = false, isPriceChanged = false, isCountNotEnough = false;
      if (!cartProduct.getName().equals(p.getName())) {
        productTmpMessages.add(cartProduct.getName() + "의 상품명이 " + p.getName() + "으로 변경되었습니다");
        cartProduct.setName(p.getName());
      }

      if (!cartProduct.getPrice().equals(p.getPrice())) {
        productTmpMessages.add(cartProduct.getName() + "의 가격이 변동되었습니다");
        cartProduct.setPrice(p.getPrice());
      }

      if (cartProduct.getCount() > p.getCount()) {
        productTmpMessages.add(
            cartProduct.getName() + "의 수량이 부족하여 구매 가능한 최대치로 변경되었습니다");
        cartProduct.setCount(p.getCount());
      }

      if (productTmpMessages.size() > 0) {
        StringBuilder productBuilder = new StringBuilder();
        productBuilder.append(cartProduct.getName() + "의 변동 사항 :\n");

        int productMessageSize = 0;
        for (String message : productTmpMessages) {
          productBuilder.append(message);
          productMessageSize++;
          if (productMessageSize != productTmpMessages.size()) {
            productBuilder.append(",\n");
          }
        }
        cart.addMessage(productBuilder.toString());
      }

      if (!ObjectUtils.isEmpty(cartProduct.getItems())) {
        List<String> itemTmpMessages = new ArrayList<>();

        Map<Long, com.kokio.entitymodule.domain.product.entity.Item> itemMap = p.getItems().stream()
            .collect(Collectors.toMap(
                com.kokio.entitymodule.domain.product.entity.Item::getId, item -> item));
        for (int j = 0; j < cartProduct.getItems().size(); j++) {
          Item cartItem = cartProduct.getItems().get(j);
          com.kokio.entitymodule.domain.product.entity.Item item = itemMap.get(cartItem.getId());
          if (ObjectUtils.isEmpty(item)) {
            cartProduct.getItems().remove(cartItem);
            j--;
            cart.addMessage(cartItem.getName() + " 옵션이 삭제되었습니다.");
            continue;
          }

          if (!cartItem.getName().equals(item.getName())) {
            itemTmpMessages.add(cartProduct.getName() + "의 상품명이 " + p.getName() + "으로 변경되었습니다.");
            cartItem.setName(item.getName());
          }
          if (!cartItem.getPrice().equals(item.getPrice())) {
            itemTmpMessages.add(cartItem.getName() + "의 가격이 변동되었습니다.");
            cartItem.setPrice(item.getPrice());
          }
          if (cartItem.getCount() > item.getCount()) {
            itemTmpMessages.add(cartItem.getName() + "의 수량이 부족하여 구매 가능한 최대치로 변경되었습니다.");
            cartItem.setCount(item.getCount());
          }
        }

        if (itemTmpMessages.size() > 0) {
          StringBuilder itemBuilder = new StringBuilder();
          itemBuilder.append(cartProduct.getName() + "의 변동 사항 :\n");
          int itemMessageSize = 0;
          for (String message : itemTmpMessages) {
            itemMessageSize++;
            itemBuilder.append(message);
            if (itemMessageSize != itemTmpMessages.size()) {
              itemBuilder.append(",\n");
            }
          }
          cart.addMessage(itemBuilder.toString());
        }

      }
    }
    return cart;
  }

  private boolean addAble(Cart cart, Product product, AddProductCartForm form) {
    Cart.Product cartProduct = cart.getProducts().stream()
        .filter(p -> p.getId().equals(form.getId()))
        .findFirst().orElseThrow(() -> new ProductException(FAIL_FIND_PRODUCT));

    Integer cartProductCount = cartProduct.getCount();
    Integer currentProductCount = product.getCount();

    Map<Long, Integer> cartItemCountMap = cartProduct.getItems().stream()
        .collect(Collectors.toMap(Item::getId, Item::getCount));

    Map<Long, Integer> currentItemCountMap = product.getItems().stream().collect(Collectors.toMap(
        com.kokio.entitymodule.domain.product.entity.Item::getId,
        com.kokio.entitymodule.domain.product.entity.Item::getCount));

    if (!form.getItems().isEmpty()) {
      return !(cartProductCount + form.getCount() > currentProductCount) &&
          form.getItems().stream().noneMatch(item -> {
                Integer cartCount = cartItemCountMap.get(item.getId());
                Integer currentCount = currentItemCountMap.get(item.getId());
                return item.getCount() + cartCount > currentCount;
              }
          );
    }

    return !(cartProductCount + form.getCount() > currentProductCount);

  }

}
