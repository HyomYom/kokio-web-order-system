package com.kokio.productapi.service;

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
import com.kokio.entitymodule.domain.product.repository.ProductRepositoryImpl;
import com.kokio.entitymodule.domain.user.model.UserDto;
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
public class SellerProductManageService {

  private final ProductRepository productRepository;
  private final ProductRepositoryImpl productRepositoryImpl;

  private final ItemRepository itemRepository;


  @Transactional
  public Product addProduct(UserDto seller, AddProductForm form) {
    Product product = AddProductForm.toProductWithItem(seller, form);
    return productRepository.save(product);
  }

  @Transactional
  public Product addItem(UserDto seller, List<AddItemForm> itemList) {
    Product findProduct = productRepository.findById(itemList.stream().findFirst().get()
            .getProductId())
        .orElseThrow(() ->
            new ProductException(FAIL_FIND_PRODUCT)
        );
    for (AddItemForm addItemForm : itemList) {
      findProduct.updateItems(AddItemForm.toItem(seller, addItemForm));
    }
    return findProduct;
  }


  @Transactional
  public Product updateProduct(UserDto seller, ProductUpdateForm form) {
    Product findProduct = productRepository.findByIdAndSellerId(form.getProductId(), seller.getId())
        .orElseThrow(
            () -> new ProductException(FAIL_FIND_PRODUCT)
        );
    if (!ObjectUtils.isEmpty(form.getName()) && !findProduct.getName().equals(form.getName())) {
      findProduct.setName(form.getName());
    }
    if (!ObjectUtils.isEmpty(form.getDescription()) && !findProduct.getDescription()
        .equals(form.getDescription())) {
      findProduct.setDescription(form.getDescription());
    }
    if (!ObjectUtils.isEmpty(form.getPrice()) && !Objects.equals(findProduct.getPrice(),
        form.getPrice())) {
      findProduct.setPrice(form.getPrice());
    }
    if (!ObjectUtils.isEmpty(form.getCount()) && !Objects.equals(findProduct.getCount(),
        form.getCount())) {
      findProduct.setCount(form.getCount());
    }
    if (!ObjectUtils.isEmpty(form.getItems())) {

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
    }
    return findProduct;
  }


  @Transactional
  public Item updateItem(UserDto seller, ItemUpdateForm form) {
    Item item = itemRepository.findById(form.getItemId())
        .filter(it -> it.getSellerId().equals(seller.getId())).orElseThrow(() ->
            new ProductException(FAIL_FIND_ITEM));
    if (!ObjectUtils.isEmpty(form.getName()) && !Objects.equals(item.getName(), form.getName())) {
      item.setName(form.getName());
    }
    if (!ObjectUtils.isEmpty(form.getPrice()) && !Objects.equals(item.getPrice(),
        form.getPrice())) {
      item.setPrice(form.getPrice());
    }
    if (!ObjectUtils.isEmpty(form.getCount()) && !Objects.equals(item.getCount(),
        form.getCount())) {
      item.setCount(form.getCount());
    }
    return item;

  }

  public boolean checkExistProduct(UserDto seller, AddProductForm form) {
    return productRepository.existsBySellerIdAndName(seller.getId(), form.getName());
  }

  @Transactional
  public boolean checkExistProductById(Long productId) {
    return productRepository.existsById(productId);
  }

  @Transactional
  public boolean checkExistItemById(Long itemId) {
    return itemRepository.existsById(itemId);
  }


  @Transactional
  public Product deleteProduct(UserDto seller, Long productId) {
    Product findProduct = productRepository.findByIdAndSellerId(productId, seller.getId())
        .orElseThrow(() -> new ProductException(FAIL_FIND_PRODUCT));
    if (!ObjectUtils.isEmpty(findProduct)) {
      productRepository.deleteById(findProduct.getId());
      return findProduct;
    }
    return null;
  }

  @Transactional
  public Item deleteItem(UserDto seller, Long productId) {
    Item findItem = itemRepository.findByIdAndSellerId(productId, seller.getId())
        .orElseThrow(() -> new ProductException(FAIL_FIND_PRODUCT));
    if (!ObjectUtils.isEmpty(findItem)) {
      itemRepository.deleteById(findItem.getId());
      return findItem;
    }
    return null;
  }

  public Page<Product> searchProduct(UserDto seller, String name, Pageable pageable) {
    return productRepository.searchByName(seller, name, pageable);

  }
}
