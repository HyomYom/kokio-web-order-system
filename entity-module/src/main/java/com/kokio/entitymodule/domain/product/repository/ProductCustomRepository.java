package com.kokio.entitymodule.domain.product.repository;

import com.kokio.entitymodule.domain.product.entity.Product;
import com.kokio.entitymodule.domain.user.model.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductCustomRepository {


  Page<Product> searchByName(UserDto seller, String productName, Pageable pageable);

  Page<Product> customerSearchByName(String productName, Pageable pageable);
}
