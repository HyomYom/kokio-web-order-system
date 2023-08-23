package com.kokio.userapi.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;


@Getter
@RequiredArgsConstructor
public enum ErrorCode {
  USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않는 사용자 입니다"),
  USER_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "이미 사용자 정보가 존재합니다.");


  private final HttpStatus httpStatus;
  private final String detail;
}
