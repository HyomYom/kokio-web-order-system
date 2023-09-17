package com.kokio.commonmodule.exception.Code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;


@Getter
@RequiredArgsConstructor
public enum CartErrorCode {
  FAIL_UPDATE_CART(HttpStatus.BAD_REQUEST, "카트가 존재하지 않습니다.");

  private final HttpStatus httpStatus;
  private final String detail;
}
