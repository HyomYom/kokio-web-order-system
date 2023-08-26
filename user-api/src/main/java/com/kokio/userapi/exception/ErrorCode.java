package com.kokio.userapi.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;


@Getter
@RequiredArgsConstructor
public enum ErrorCode {
  USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않는 사용자 입니다"),
  USER_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "이미 사용자 정보가 존재합니다."),

  ALREADY_VERIFY(HttpStatus.BAD_REQUEST, "이미 확인되었습니다."),
  WRONG_VERIFICATION_CODE(HttpStatus.BAD_REQUEST, "인증코드가 일치하지 않습니다."),
  PASSWORD_NOT_MATCHED(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),

  VERIFICATION_PERIOD_HAS_EXPIRED(HttpStatus.BAD_REQUEST, "인증 기간이 만료됐습니다."),

  //mailGun
  FAIL_TO_SEND_EMAIL(HttpStatus.BAD_REQUEST, "이메일 전송중 오류가 발생하였습니다."),

  //token
  JWT_TOKEN_EXPIRED(HttpStatus.BAD_REQUEST, "토큰이 더이상 유요하지 않습니다.");


  private final HttpStatus httpStatus;
  private final String detail;
}
