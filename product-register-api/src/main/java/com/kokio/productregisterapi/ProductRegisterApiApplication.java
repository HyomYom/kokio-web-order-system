package com.kokio.productregisterapi;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@RequiredArgsConstructor
@SpringBootApplication
public class ProductRegisterApiApplication {


  public static void main(String[] args) {
    SpringApplication.run(ProductRegisterApiApplication.class, args);
  }

}
