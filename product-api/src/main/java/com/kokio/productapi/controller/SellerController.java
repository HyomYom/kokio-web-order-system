package com.kokio.productapi.controller;

import com.kokio.commonmodule.utill.PageRequest;
import com.kokio.entitymodule.domain.product.model.AddItemForm;
import com.kokio.entitymodule.domain.product.model.AddProductForm;
import com.kokio.entitymodule.domain.product.model.ItemDto;
import com.kokio.entitymodule.domain.product.model.ItemUpdateForm;
import com.kokio.entitymodule.domain.product.model.ProductDto;
import com.kokio.entitymodule.domain.product.model.ProductUpdateForm;
import com.kokio.entitymodule.domain.user.model.UserDto;
import com.kokio.productapi.application.SellerProductManageApplication;
import com.kokio.productapi.client.UserClient;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/seller")
public class SellerController {

  private final SellerProductManageApplication sellerProductManageApplication;
  private final UserClient userClient;


  @PostMapping("/addProduct")
  @PreAuthorize("hasRole('SELLER')")
  public ResponseEntity<?> addProduct(@RequestHeader(name = "Authorization") String token,
      @RequestBody AddProductForm form) {
    UserDto seller = userClient.getUserInfoForModule(token).getBody();

    return ResponseEntity.ok(sellerProductManageApplication.addProduct(seller, form));

  }

  @PostMapping("/addItem")
  @PreAuthorize("hasRole('SELLER')")
  public ResponseEntity<ProductDto> addItem(@RequestHeader(name = "Authorization") String token,
      @RequestBody List<AddItemForm> formList) {
    UserDto seller = userClient.getUserInfoForModule(token).getBody();

    return ResponseEntity.ok(
        ProductDto.toDto(sellerProductManageApplication.addItem(seller, formList)));
  }

  @PutMapping("/update/product")
  @PreAuthorize("hasRole('SELLER')")
  public ResponseEntity<?> updateProduct(@RequestHeader(name = "Authorization") String token,
      @RequestBody ProductUpdateForm form) {
    UserDto seller = userClient.getUserInfoForModule(token).getBody();

    return ResponseEntity.ok(
        ProductDto.toDto(sellerProductManageApplication.updateProduct(seller, form)));
  }

  @PutMapping("/update/item")
  @PreAuthorize("hasRole('SELLER')")
  public ResponseEntity<?> updateItem(@RequestHeader(name = "Authorization") String token,
      @RequestBody ItemUpdateForm form) {
    UserDto seller = userClient.getUserInfoForModule(token).getBody();

    return ResponseEntity.ok(
        ItemDto.toDto(sellerProductManageApplication.updateItem(seller, form)));

  }

  @DeleteMapping("/delete/product")
  @PreAuthorize("hasRole('SELLER')")
  public ResponseEntity<?> deleteProduct(@RequestHeader(name = "Authorization") String token,
      @RequestParam Long productId) {
    UserDto seller = userClient.getUserInfoForModule(token).getBody();

    return ResponseEntity.ok(
        ProductDto.toDto(sellerProductManageApplication.deleteProduct(seller, productId)));

  }


  @DeleteMapping("/delete/item")
  @PreAuthorize("hasRole('SELLER')")
  public ResponseEntity<?> deleteItem(@RequestHeader(name = "Authorization") String token,
      @RequestParam Long itemId) {
    UserDto seller = userClient.getUserInfoForModule(token).getBody();

    return ResponseEntity.ok(
        ItemDto.toDto(sellerProductManageApplication.deleteItem(seller, itemId)));

  }

  @GetMapping("/search")
  @PreAuthorize("hasRole('SELLER')")
  public ResponseEntity<?> searchProduct(@RequestHeader(name = "Authorization") String token,
      @RequestParam String name, @RequestParam int page, @RequestParam int size,
      @RequestParam String dir, @RequestParam String sort, Pageable pageable

  ) {
    UserDto seller = userClient.getUserInfoForModule(token).getBody();

    PageRequest pageRequest = new PageRequest();
    pageRequest.setPage(page);
    pageRequest.setSize(size);
    if (sort != null && dir != null && !sort.equals("") && !dir.equals("")) {
      if (dir.equals("desc")) {
        pageable = pageRequest.of(Sort.Direction.DESC, sort);
      } else {

        pageable = pageRequest.of(Sort.Direction.ASC, sort);
      }
    } else {
      pageable = pageRequest.of();
    }

    return ResponseEntity.ok(
        sellerProductManageApplication.searchProduct(seller, name, pageable)
            .map(ProductDto::toDtoWithOutItem));

  }
}
