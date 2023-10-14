package com.kokio.userapi.controller;


import static com.kokio.commonmodule.exception.Code.UserErrorCode.USER_NOT_FOUND;

import com.kokio.commonmodule.exception.UserException;
import com.kokio.commonmodule.security.TokenProvider;
import com.kokio.commonmodule.security.common.UserVo;
import com.kokio.entitymodule.domain.user.entity.User;
import com.kokio.entitymodule.domain.user.model.Sign;
import com.kokio.entitymodule.domain.user.model.UserDto;
import com.kokio.userapi.application.SignInApplication;
import com.kokio.userapi.application.SignUpApplication;
import com.kokio.userapi.model.security.Token;
import com.kokio.userapi.service.author.AuthorGetUserInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
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
@Slf4j
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

  private final SignUpApplication signUpApplication;
  private final SignInApplication signInApplication;
  private final AuthorGetUserInfoService authorGetUserInfoService;
  private final TokenProvider provider;


  @PostMapping("/register")
  public ResponseEntity<?> userSignUp(@RequestBody Sign.Up form) {
    return ResponseEntity.ok(signUpApplication.userSignUp(form));

  }

  @GetMapping("/register/verify")
  public ResponseEntity<String> userSignUpVerify(@RequestParam(name = "email") String email,
      @RequestParam(name = "code") String code) {
    return ResponseEntity.ok(signUpApplication.userVerify(email, code));
  }

  @PostMapping("/login")
  public ResponseEntity<String> userLogin(@RequestBody Sign.In form) {

    Token tokens = signInApplication.userTokenLogin(form);
    ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", tokens.getAccessToken())
        .httpOnly(true).secure(true).path("/").build();

    ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken",
        tokens.getRefreshToken()).httpOnly(true).secure(true).path("/").build();

    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
    headers.add(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());

    return ResponseEntity.ok().headers(headers).build();
  }


  @GetMapping("/getInfo")
  @PreAuthorize("hasRole('CUSTOMER')")
  public ResponseEntity<UserDto> getUserInfo(@RequestHeader("Cookie") String cookies) {
    String token = provider.extractAccessTokenFromCookies(cookies);
    UserVo userVo = provider.getUserVo(token);
    UserDetails userDetails = authorGetUserInfoService.getUserInfo(userVo.getId(),
        userVo.getEmail()).orElseThrow(() -> new UserException(USER_NOT_FOUND));
    return ResponseEntity.ok(UserDto.toDto((User) userDetails));
  }


  @GetMapping("/getInfo/module")
  @PreAuthorize("hasRole('SELLER')")
  public ResponseEntity<UserDto> getUserInfoForModule(@RequestHeader("Cookie") String cookies) {
    String token = provider.extractAccessTokenFromCookies(cookies);

    UserVo userVo = provider.getUserVo(token);
    User user = authorGetUserInfoService.getUserInfo(userVo.getId(), userVo.getEmail())
        .orElseThrow(() -> new UserException(USER_NOT_FOUND));
    return ResponseEntity.ok(UserDto.toDto(user));
  }

  @PostMapping("/refresh_token")
  @PreAuthorize("hasRole('CUSTOMER')")
  public ResponseEntity<?> refreshToken(@RequestHeader("Cookie") String cookies) {
    String refreshToken = provider.extractRefreshTokenFromCookies(cookies);
    String newAccessToken = provider.createNewAccessToken(refreshToken);
    ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", newAccessToken)
        .httpOnly(true).secure(true).path("/").build();

    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
    return ResponseEntity.ok().headers(headers).build();
  }


}
