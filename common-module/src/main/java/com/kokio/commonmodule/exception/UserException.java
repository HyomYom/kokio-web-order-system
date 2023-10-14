package com.kokio.commonmodule.exception;

import com.kokio.commonmodule.exception.Code.UserErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserException extends RuntimeException {

  private UserErrorCode userErrorCode;

  private int status;

  public UserException(UserErrorCode userErrorCode) {
    super(userErrorCode.getDetail()); // 부모 메소드를 초기화하여 재정의
    this.userErrorCode = userErrorCode;
    this.status = userErrorCode.getHttpStatus().value();
  }

  public UserException(UserErrorCode userErrorCode, String error) {
    super(error);
    this.userErrorCode = userErrorCode;
    this.status = userErrorCode.getHttpStatus().value();

  }


}
