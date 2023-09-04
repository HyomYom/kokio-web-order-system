package com.kokio.commonmodule.exception.Code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;


@Getter
@RequiredArgsConstructor
public enum ProductErrorCode {
  FAIL_ADD_PRODUCT(HttpStatus.BAD_REQUEST, "상품을 등록하지 못했습니다."),
  PRODUCT_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "이미 등록된 상품입니다."),
  FAIL_FIND_PRODUCT(HttpStatus.BAD_REQUEST, "등록된 상품을 찾이 못하였습니다."),
  FAIL_FIND_ITEM(HttpStatus.BAD_REQUEST, "등록된 세부상품을 찾지 못하였습니다."),
  USER_INFO_RETRIEVAL_FAILED(HttpStatus.BAD_REQUEST, "유저 정보를 불러오지 못했습니다."),
  FAIL_UPDATE_PRODUCT_NAME(HttpStatus.BAD_REQUEST, "상품명 변경에 실패했습니다."),
  FAIL_UPDATE_PRODUCT_PRICE(HttpStatus.BAD_REQUEST, "상품가격 변경에 실패했습니다."),
  FAIL_UPDATE_PRODUCT_COUNT(HttpStatus.BAD_REQUEST, "상품수량 변경에 실패했습니다."),
  FAIL_UPDATE_ITEM_NAME(HttpStatus.BAD_REQUEST, "상세 상품명 변경에 실패했습니다."),
  FAIL_UPDATE_ITEM_PRICE(
      HttpStatus.BAD_REQUEST, "상세 상품가격 변경에 실패했습니다."),
  FAIL_UPDATE_ITEM_COUNT(
      HttpStatus.BAD_REQUEST, "상세 상품수량 변경에 실패했습니다."),

  //토큰
  TOKEN_AUTHENTICATION_TIMEOUT(HttpStatus.BAD_REQUEST, "토큰 유효 기간 만료");

  private final HttpStatus httpStatus;
  private final String detail;
}