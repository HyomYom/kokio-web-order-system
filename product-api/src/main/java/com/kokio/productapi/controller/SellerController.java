package com.kokio.productapi.controller;

import com.kokio.entitymodule.domain.product.model.AddItemForm;
import com.kokio.entitymodule.domain.product.model.AddProductForm;
import com.kokio.entitymodule.domain.product.model.ProductDto;
import com.kokio.entitymodule.domain.product.model.ProductUpdateForm;
import com.kokio.entitymodule.domain.user.model.UserDto;
import com.kokio.productapi.application.ProductManageApplication;
import com.kokio.productapi.utill.UserUtility;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/seller")
public class SellerController {

  private final ProductManageApplication productManageApplication;
  private final UserUtility userUtility;

  @PostMapping("/addProduct")
  @PreAuthorize("hasRole('SELLER')")
  public ResponseEntity<?> addProduct(@RequestHeader(name = "Authorization") String token,
      @RequestBody AddProductForm form
  ) {
    UserDto seller = userUtility.getUserInfoForModule(token).getBody();

    return ResponseEntity.ok(productManageApplication.addProduct(seller, form));

  }

  @PostMapping("/addItem")
  @PreAuthorize("hasRole('SELLER')")
  public ResponseEntity<ProductDto> addItem(@RequestHeader(name = "Authorization") String token,
      @RequestBody List<AddItemForm> formList
  ) {
    UserDto seller = userUtility.getUserInfoForModule(token).getBody();

    return ResponseEntity.ok(ProductDto.toDto(productManageApplication.addItem(seller, formList)));
  }

  @PutMapping("/update/product")
  @PreAuthorize("hasRole('SELLER')")
  public ResponseEntity<?> updateProduct(@RequestHeader(name = "Authorization") String token,
      @RequestBody ProductUpdateForm form
  ) {
    UserDto seller = userUtility.getUserInfoForModule(token).getBody();

    return ResponseEntity.ok(
        ProductDto.toDto(productManageApplication.updateProduct(seller, form)));
  }

}
