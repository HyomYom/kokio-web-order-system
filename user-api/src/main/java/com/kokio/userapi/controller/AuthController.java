package com.kokio.userapi.controller;


import static com.kokio.userapi.exception.ErrorCode.USER_NOT_FOUND;

import com.kokio.tokenprovider.security.TokenProvider;
import com.kokio.tokenprovider.security.common.UserVo;
import com.kokio.userapi.application.SignInApplication;
import com.kokio.userapi.application.SignUpApplication;
import com.kokio.userapi.domain.model.Sign;
import com.kokio.userapi.exception.CustomException;
import com.kokio.userapi.service.author.AuthorGetUserInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

  private final SignUpApplication signUpApplication;
  private final SignInApplication signInApplication;
  private final AuthorGetUserInfoService authorGetUserInfoService;
  private final TokenProvider provider;
  public static final String TOKEN_PREFIX = "Bearer ";


  @PostMapping("/register")
  public ResponseEntity<String> userSignUp(@RequestBody Sign.Up form) {
    return ResponseEntity.ok(signUpApplication.userSignUp(form));

  }

  @GetMapping("/register/verify")
  public ResponseEntity<String> userSignUpVerify(@RequestParam(name = "email") String email,
      @RequestParam(name = "code") String code) {
    return ResponseEntity.ok(signUpApplication.userVerify(email, code));
  }

  @PostMapping("/login")
  public ResponseEntity<String> userLogin(@RequestBody Sign.In form) {

    return ResponseEntity.ok(signInApplication.userTokenLogin(form));
  }

  @GetMapping("/getInfo")
  @PreAuthorize("hasRole('CUSTOMER')")
  public ResponseEntity<UserDetails> getUserInfo(
      @RequestHeader(name = "Authorization") String token) {
    String retoken = token.substring(TOKEN_PREFIX.length());
    UserVo userVo = provider.getUserVo(retoken);
    UserDetails userDetails = authorGetUserInfoService.getUserInfo(userVo.getId(),
        userVo.getEmail()).orElseThrow(
        () -> new CustomException(USER_NOT_FOUND)
    );
    return ResponseEntity.ok(userDetails);
  }
}
