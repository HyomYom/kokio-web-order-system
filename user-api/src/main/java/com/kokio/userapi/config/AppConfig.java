package com.kokio.userapi.config;

import com.kokio.commonmodule.exception.ExceptionController;
import com.kokio.commonmodule.globalConfig.GlobalAppConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AppConfig extends GlobalAppConfig {

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public ExceptionController exceptionController() {
    return new ExceptionController();
  }


}
