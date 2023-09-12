package com.kokio.productapi.application;


import static com.kokio.commonmodule.exception.Code.ProductErrorCode.FAIL_ADD_PRODUCT;
import static com.kokio.commonmodule.exception.Code.ProductErrorCode.FAIL_DELETE_ITEM;
import static com.kokio.commonmodule.exception.Code.ProductErrorCode.FAIL_DELETE_PRODUCT;
import static com.kokio.commonmodule.exception.Code.ProductErrorCode.FAIL_FIND_ITEM;
import static com.kokio.commonmodule.exception.Code.ProductErrorCode.FAIL_FIND_PRODUCT;
import static com.kokio.commonmodule.exception.Code.ProductErrorCode.FAIL_UPDATE_ITEM_COUNT;
import static com.kokio.commonmodule.exception.Code.ProductErrorCode.FAIL_UPDATE_ITEM_NAME;
import static com.kokio.commonmodule.exception.Code.ProductErrorCode.FAIL_UPDATE_ITEM_PRICE;
import static com.kokio.commonmodule.exception.Code.ProductErrorCode.FAIL_UPDATE_PRODUCT_COUNT;
import static com.kokio.commonmodule.exception.Code.ProductErrorCode.FAIL_UPDATE_PRODUCT_NAME;
import static com.kokio.commonmodule.exception.Code.ProductErrorCode.FAIL_UPDATE_PRODUCT_PRICE;
import static com.kokio.commonmodule.exception.Code.ProductErrorCode.PRODUCT_ALREADY_EXIST;

import com.kokio.commonmodule.exception.ProductException;
import com.kokio.entitymodule.domain.product.entity.Item;
import com.kokio.entitymodule.domain.product.entity.Product;
import com.kokio.entitymodule.domain.product.model.AddItemForm;
import com.kokio.entitymodule.domain.product.model.AddProductForm;
import com.kokio.entitymodule.domain.product.model.ItemUpdateForm;
import com.kokio.entitymodule.domain.product.model.ProductUpdateForm;
import com.kokio.entitymodule.domain.user.model.UserDto;
import com.kokio.productapi.service.SellerProductManageService;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

@Service
@RequiredArgsConstructor
public class SellerProductManageApplication {

  private final SellerProductManageService sellerProductManageService;


  @Transactional
  public Product addProduct(UserDto seller, AddProductForm form) {
    if (sellerProductManageService.checkExistProduct(seller, form)) {
      throw new ProductException(PRODUCT_ALREADY_EXIST);
    }
    Product product = sellerProductManageService.addProduct(seller, form);
    if (ObjectUtils.isEmpty(product)) {
      throw new ProductException(FAIL_ADD_PRODUCT);
    }
    return product;
  }


  public Product addItem(UserDto seller, List<AddItemForm> formList) {
    return sellerProductManageService.addItem(seller, formList);
  }


  @Transactional
  public Product updateProduct(UserDto seller, ProductUpdateForm form) {
    Product product = sellerProductManageService.updateProduct(seller, form);

    if (!product.getName().equals(form.getName())) {
      throw new ProductException(FAIL_UPDATE_PRODUCT_NAME);
    } else if (!Objects.equals(product.getPrice(), form.getPrice())) {
      throw new ProductException(FAIL_UPDATE_PRODUCT_PRICE);
    } else if (!Objects.equals(product.getCount(), form.getCount())) {
      throw new ProductException(FAIL_UPDATE_PRODUCT_COUNT);
    }
    if (!ObjectUtils.isEmpty(form.getItems())) {

      for (ItemUpdateForm itemUpdateForm : form.getItems()) {
        Item item = product.getItems().stream()
            .filter(it -> it.getId().equals(itemUpdateForm.getItemId())).findFirst()
            .orElseThrow(() -> new ProductException(FAIL_FIND_ITEM));

        if (!Objects.equals(item.getName(), itemUpdateForm.getName())) {
          throw new ProductException(FAIL_UPDATE_ITEM_NAME);
        } else if (!Objects.equals(item.getPrice(), itemUpdateForm.getPrice())) {
          throw new ProductException(FAIL_UPDATE_ITEM_PRICE);
        } else if (!Objects.equals(item.getCount(), itemUpdateForm.getCount())) {
          throw new ProductException(FAIL_UPDATE_ITEM_COUNT);
        }
      }
    }

    return product;

  }

  @Transactional
  public Item updateItem(UserDto seller, ItemUpdateForm form) {
    Item item = sellerProductManageService.updateItem(seller, form);

    if (!Objects.equals(item.getName(), form.getName())) {
      throw new ProductException(FAIL_UPDATE_ITEM_NAME);
    } else if (!Objects.equals(item.getPrice(), form.getPrice())) {
      throw new ProductException(FAIL_UPDATE_ITEM_PRICE);
    } else if (!Objects.equals(item.getCount(), form.getCount())) {
      throw new ProductException(FAIL_UPDATE_ITEM_COUNT);
    }
    return item;
  }


  @Transactional
  public Product deleteProduct(UserDto seller, Long productId) {
    if (sellerProductManageService.checkExistProductById(productId)) {
      Product deletedProduct = sellerProductManageService.deleteProduct(seller, productId);
      if (ObjectUtils.isEmpty(deletedProduct)) {
        throw new ProductException(FAIL_DELETE_PRODUCT);
      }
      return deletedProduct;
    } else {
      throw new ProductException(FAIL_FIND_PRODUCT);
    }
  }

  @Transactional
  public Item deleteItem(UserDto seller, Long itemId) {
    if (sellerProductManageService.checkExistItemById(itemId)) {
      Item deletedItem = sellerProductManageService.deleteItem(seller, itemId);
      if (ObjectUtils.isEmpty(deletedItem)) {
        throw new ProductException(FAIL_DELETE_ITEM);
      }
      return deletedItem;
    } else {
      throw new ProductException(FAIL_FIND_ITEM);
    }
  }

  public Page<Product> searchProduct(UserDto seller, String name, Pageable pageable) {
    return sellerProductManageService.searchProduct(seller, name, pageable);
  }
}