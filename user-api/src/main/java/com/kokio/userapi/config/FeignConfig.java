package com.kokio.userapi.config;


import feign.auth.BasicAuthRequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

  @Value("${spring.mailgun.key}")
  private String MAILGUN_KEY;

  @Bean
  public BasicAuthRequestInterceptor basicAuthRequestInterceptor() {

    return new BasicAuthRequestInterceptor("api", MAILGUN_KEY);
  }

}
