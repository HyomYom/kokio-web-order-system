package com.kokio.productapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@ServletComponentScan
@EnableFeignClients
@EnableJpaAuditing
@SpringBootApplication
public class ProductApiApplication {


  public static void main(String[] args) {
    SpringApplication.run(ProductApiApplication.class, args);
  }

}
