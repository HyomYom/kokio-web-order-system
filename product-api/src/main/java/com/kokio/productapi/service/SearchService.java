package com.kokio.productapi.service;


import static com.kokio.commonmodule.exception.Code.ProductErrorCode.FAIL_FIND_PRODUCT;

import com.kokio.commonmodule.exception.ProductException;
import com.kokio.entitymodule.domain.product.entity.Item;
import com.kokio.entitymodule.domain.product.entity.Product;
import com.kokio.entitymodule.domain.product.repository.ItemRepository;
import com.kokio.entitymodule.domain.product.repository.ProductRepository;
import com.kokio.entitymodule.domain.product.repository.ProductRepositoryImpl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchService {

  private final ProductRepository productRepository;
  private final ItemRepository itemRepository;
  private final ProductRepositoryImpl productRepositoryImpl;

  public Page<Product> searchByName(String name, Pageable pageable) {
    Page<Product> products = productRepositoryImpl.customerSearchByName(name, pageable);
    return products;
  }

  public Product getDetail(Long productId) {
    return productRepository.findById(productId).orElseThrow(() ->
        new ProductException(FAIL_FIND_PRODUCT)
    );
  }

  public Product getByProductId(Long productId) {
    return productRepository.getById(productId);
  }

  public List<Product> getListByProductIds(List<Long> productIds) {
    return productRepository.findAllByIdIn(productIds);
  }

  public List<Item> getListByItemIds(List<Long> itemIds) {
    return itemRepository.findAllByIdIn(itemIds);
  }

}
