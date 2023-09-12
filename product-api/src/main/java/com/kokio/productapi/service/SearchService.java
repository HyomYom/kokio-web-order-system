package com.kokio.productapi.service;


import static com.kokio.commonmodule.exception.Code.ProductErrorCode.FAIL_FIND_PRODUCT;

import com.kokio.commonmodule.exception.ProductException;
import com.kokio.entitymodule.domain.product.entity.Product;
import com.kokio.entitymodule.domain.product.repository.ProductRepository;
import com.kokio.entitymodule.domain.product.repository.ProductRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchService {

  private final ProductRepository productRepository;
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

}
