package com.kokio.productapi.application;


import static com.kokio.commonmodule.exception.Code.ProductErrorCode.FAIL_ADD_PRODUCT;
import static com.kokio.commonmodule.exception.Code.ProductErrorCode.FAIL_FIND_ITEM;
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
import com.kokio.productapi.service.ProductManageService;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

@Service
@RequiredArgsConstructor
public class ProductManageApplication {

  private final ProductManageService productManageService;

  public Product addProduct(UserDto seller, AddProductForm form) {
    if (productManageService.checkExistProduct(seller, form)) {
      throw new ProductException(PRODUCT_ALREADY_EXIST);
    }
    Product product = productManageService.addProduct(seller, form);
    if (ObjectUtils.isEmpty(product)) {
      new ProductException(FAIL_ADD_PRODUCT);
    }
    return product;
  }


  public Product addItem(UserDto seller, List<AddItemForm> formList) {
    return productManageService.updateProductItem(seller, formList);
  }


  @Transactional
  public Product updateProduct(UserDto seller, ProductUpdateForm form) {
    Product product = productManageService.updateProduct(seller, form);

    if (!product.getName().equals(form.getName())) {
      throw new ProductException(FAIL_UPDATE_PRODUCT_NAME);
    } else if (!Objects.equals(product.getPrice(), form.getPrice())) {
      throw new ProductException(FAIL_UPDATE_PRODUCT_PRICE);
    } else if (!Objects.equals(product.getCount(), form.getCount())) {
      throw new ProductException(FAIL_UPDATE_PRODUCT_COUNT);
    }
    for (Item item : product.getItems()) {
      ItemUpdateForm updateForm = form.getItems().stream().filter(iuf ->
          iuf.getItemId().equals(item.getId())
      ).findFirst().orElseThrow(() -> new ProductException(FAIL_FIND_ITEM));

      if (!item.getName().equals(updateForm.getName())) {
        throw new ProductException(FAIL_UPDATE_ITEM_NAME);
      } else if (!Objects.equals(item.getPrice(), updateForm.getPrice())) {
        throw new ProductException(FAIL_UPDATE_ITEM_PRICE);
      } else if (!Objects.equals(item.getCount(), updateForm.getCount())) {
        throw new ProductException(FAIL_UPDATE_ITEM_COUNT);
      }
    }

    return product;

  }


}
