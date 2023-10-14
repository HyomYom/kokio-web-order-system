package com.kokio.userapi.model.security;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class Token {

  private String accessToken;
  private String refreshToken;

  public Token(String accessToken, String refreshToken) {
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
  }


}
