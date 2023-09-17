package com.kokio.commonmodule.exception;

import com.kokio.commonmodule.exception.Code.CartErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CartException extends RuntimeException {

  private CartErrorCode cartErrorCode;

  private int status;

  public CartException(CartErrorCode cartErrorCode) {
    super(cartErrorCode.getDetail()); // 부모 메소드를 초기화하여 재정의
    this.cartErrorCode = cartErrorCode;
    this.status = cartErrorCode.getHttpStatus().value();
  }


}
