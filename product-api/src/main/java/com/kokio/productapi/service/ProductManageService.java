package com.kokio.productapi.service;

import static com.kokio.commonmodule.exception.Code.ProductErrorCode.FAIL_ADD_PRODUCT;
import static com.kokio.commonmodule.exception.Code.ProductErrorCode.FAIL_FIND_ITEM;
import static com.kokio.commonmodule.exception.Code.ProductErrorCode.FAIL_FIND_PRODUCT;

import com.kokio.commonmodule.exception.ProductException;
import com.kokio.entitymodule.domain.product.entity.Item;
import com.kokio.entitymodule.domain.product.entity.Product;
import com.kokio.entitymodule.domain.product.model.AddItemForm;
import com.kokio.entitymodule.domain.product.model.AddProductForm;
import com.kokio.entitymodule.domain.product.model.ItemUpdateForm;
import com.kokio.entitymodule.domain.product.model.ProductUpdateForm;
import com.kokio.entitymodule.domain.product.repository.ItemRepository;
import com.kokio.entitymodule.domain.product.repository.ProductRepository;
import com.kokio.entitymodule.domain.user.model.UserDto;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

@Service
@RequiredArgsConstructor
public class ProductManageService {

  private final ProductRepository productRepository;
  private final ItemRepository itemRepository;


  @Transactional
  public Product addProduct(UserDto seller, AddProductForm form) {
    Product product = AddProductForm.toProductWithItem(seller, form);
    return productRepository.save(product);
  }

  @Transactional
  public Product updateProductItem(UserDto seller, List<AddItemForm> itemList) {
    Product findProduct = productRepository.findById(itemList.stream().findFirst().get()
            .getProductId())
        .orElseThrow(() ->
            new ProductException(FAIL_ADD_PRODUCT)
        );
    for (AddItemForm addItemForm : itemList) {
      findProduct.updateItems(AddItemForm.toItem(seller, addItemForm));
    }
    return findProduct;
  }

  @Transactional
  public Item addItem(Item item) {
    return itemRepository.save(item);
  }


  public Product updateProduct(UserDto seller, ProductUpdateForm form) {
    Product findProduct = productRepository.findByIdAndSellerId(seller.getId(), form.getProductId())
        .orElseThrow(
            () -> new ProductException(FAIL_FIND_PRODUCT)
        );
    if (!ObjectUtils.isEmpty(form.getName()) && !findProduct.getName().equals(form.getName())) {
      findProduct.setName(form.getName());
    }
    if (!ObjectUtils.isEmpty(form.getDescription()) && !findProduct.getName()
        .equals(form.getDescription())) {
      findProduct.setName(form.getDescription());
    }
    if (!ObjectUtils.isEmpty(form.getPrice()) && findProduct.getPrice() != form.getPrice()) {
      findProduct.setName(form.getName());
    }
    if (!ObjectUtils.isEmpty(form.getCount()) && findProduct.getCount() != form.getCount()) {
      findProduct.setName(form.getName());
    }
    for (ItemUpdateForm updateItemForm : form.getItems()) {
      Item findItem = findProduct.getItems().stream()
          .filter(it -> it.getId().equals(updateItemForm.getItemId()))
          .findFirst().orElseThrow(() -> new ProductException(FAIL_FIND_ITEM));

      if (!ObjectUtils.isEmpty(updateItemForm.getName()) && !findItem.getName()
          .equals(updateItemForm.getName())) {
        findItem.setName(updateItemForm.getName());
      }
      if (!ObjectUtils.isEmpty(updateItemForm.getPrice())
          && !Objects.equals(findItem.getPrice(), updateItemForm.getPrice())) {
        findItem.setPrice(updateItemForm.getPrice());
      }
      if (!ObjectUtils.isEmpty(updateItemForm.getCount())
          && !Objects.equals(findItem.getCount(), updateItemForm.getCount())) {
        findItem.setCount(updateItemForm.getCount());
      }

    }
    return findProduct;
  }


  public boolean checkExistProduct(UserDto seller, AddProductForm form) {
    return productRepository.existsBySellerIdAndName(seller.getId(), form.getName());
  }
}
