package com.kokio.productapi.controller;


import com.kokio.entitymodule.domain.product.model.AddProductCartForm;
import com.kokio.entitymodule.domain.redis.Cart;
import com.kokio.entitymodule.domain.user.model.UserDto;
import com.kokio.productapi.application.UserCartApplication;
import com.kokio.productapi.client.UserClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/cart")
@RequiredArgsConstructor
public class UserCartController {

  private final UserClient userClient;
  private final UserCartApplication userCartApplication;


  @PostMapping
  @PreAuthorize("hasRole('CUSTOMER')")
  public ResponseEntity<?> addCart(@RequestHeader(name = "Authorization") String token,
      @RequestBody AddProductCartForm form) {
    UserDto user = userClient.getUserInfoForModule(token).getBody();
    return ResponseEntity.ok(userCartApplication.addCart(user, form));
  }

  @GetMapping
  @PreAuthorize("hasRole('CUSTOMER')")
  public ResponseEntity<?> getCart(@RequestHeader(name = "Authorization") String token) {
    UserDto user = userClient.getUserInfoForModule(token).getBody();
    return ResponseEntity.ok(userCartApplication.getCart(user));
  }

  @PutMapping
  @PreAuthorize("hasRole('CUSTOMER')")
  public ResponseEntity<?> updateCart(@RequestHeader(name = "Authorization") String token
      , @RequestBody Cart cart
  ) {
    UserDto user = userClient.getUserInfoForModule(token).getBody();
    return ResponseEntity.ok(userCartApplication.updateCart(user, cart));
  }

}
