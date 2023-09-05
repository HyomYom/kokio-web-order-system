package com.kokio.productapi.config;

import com.kokio.commonmodule.exception.ExceptionController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

  @Bean
  public ExceptionController exceptionController() {
    return new ExceptionController();
  }

}
