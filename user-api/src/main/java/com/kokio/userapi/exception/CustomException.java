package com.kokio.userapi.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
  private final ErrorCode errorCode;
  private final int status;

  public CustomException(ErrorCode errorCode){
    super(errorCode.getDetail()); // 부모 메소드를 초기화하여 재정의
    this.errorCode = errorCode;
    this.status = errorCode.getHttpStatus().value();
  }

}
