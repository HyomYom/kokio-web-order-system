package com.kokio.userapi.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class AuthController {

  @PostMapping("/register/customer")
  public ResponseEntity<?> customerSignUp(){
    return null;
  }

//  @PostMapping("register/seller")

}
