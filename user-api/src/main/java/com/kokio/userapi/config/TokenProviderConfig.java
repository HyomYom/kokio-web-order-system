package com.kokio.userapi.config;

import com.kokio.commonmodule.security.TokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TokenProviderConfig {

  @Bean
  public TokenProvider tokenProvider() {
    return new TokenProvider();
  }

}
