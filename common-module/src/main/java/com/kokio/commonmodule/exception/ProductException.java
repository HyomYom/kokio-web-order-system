package com.kokio.commonmodule.exception;

import com.kokio.commonmodule.exception.Code.ProductErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor

public class ProductException extends RuntimeException {

  private int status;
  private ProductErrorCode productErrorCode;

  public ProductException(ProductErrorCode productErrorCode) {
    super(productErrorCode.getDetail());
    this.productErrorCode = productErrorCode;
    this.status = productErrorCode.getHttpStatus().value();
  }

}
