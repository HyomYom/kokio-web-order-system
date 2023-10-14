package com.kokio.commonmodule.security;


import static com.kokio.commonmodule.exception.Code.UserErrorCode.JWT_ACCESS_DENIED;
import static com.kokio.commonmodule.exception.Code.UserErrorCode.JWT_TOKEN_EXPIRED;

import com.kokio.commonmodule.exception.UserException;
import com.kokio.commonmodule.security.common.UserVo;
import com.kokio.commonmodule.utill.SecureAes256Util;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;


@Component
@RequiredArgsConstructor
@Slf4j
public class TokenProvider {


  private static final long TOKEN_EXPIRE_TIME = 1000 * 60 * 60;
  private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24;
  private static final String KEY_ROLES = "roles";

  @Value("${spring.jwt.secret}")
  private String secretKey;
  private SecretKey aesKeySpec;

  @PostConstruct
  public void init() {
    aesKeySpec = new SecretKeySpec(
        Arrays.copyOf(secretKey.getBytes(StandardCharsets.UTF_8), 16), "AES");
  }

  public String createToken(String userEmail, Long userId, List<String> roles) {
    Claims claims = Jwts.claims().setSubject(SecureAes256Util.encrypt(aesKeySpec, userEmail))
        .setId(SecureAes256Util.encrypt(aesKeySpec, String.valueOf(userId)));

    claims.put(KEY_ROLES, roles);
    Date now = new Date();
    return Jwts.builder().setClaims(claims).setIssuedAt(now)
        .setExpiration(new Date(now.getTime() + TOKEN_EXPIRE_TIME))
        .signWith(SignatureAlgorithm.HS256, this.secretKey).compact();
  }

  public String createRefreshToken(String userEmail, Long userId, List<String> roles) {
    Claims claims = Jwts.claims().setSubject(SecureAes256Util.encrypt(aesKeySpec, userEmail))
        .setId(SecureAes256Util.encrypt(aesKeySpec, String.valueOf(userId)));

    claims.put(KEY_ROLES, roles);
    Date now = new Date();
    return Jwts.builder().setClaims(claims).setIssuedAt(now)
        .setExpiration(new Date(now.getTime() + REFRESH_TOKEN_EXPIRE_TIME))
        .signWith(SignatureAlgorithm.HS256, this.secretKey).compact();
  }

  public String createNewAccessToken(String refreshToken) {
    if (validateToken(refreshToken)) {
      UserVo userVo = getUserVo(refreshToken);
      return createToken(userVo.getEmail(), userVo.getId(), userVo.getRoles());
    }
    return null;
  }

  public boolean validateToken(String token) {
    if (!StringUtils.hasText(token)) {
      return false;
    }

    Claims claims = claimParser(token);
    return !claims.getExpiration().before(new Date());
  }

  public UserVo getUserVo(String token) {
    Claims claims = claimParser(token);
    return new UserVo(Long.valueOf(SecureAes256Util.decrypt(aesKeySpec, claims.getId())),
        SecureAes256Util.decrypt(aesKeySpec, claims.getSubject()),
        (List<String>) claims.get(KEY_ROLES));
  }

  public Claims claimParser(String token) {
    try {
      return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    } catch (ExpiredJwtException e) {
      throw new UserException(JWT_TOKEN_EXPIRED);
    } catch (UnsupportedJwtException e) {
      throw new UserException(JWT_ACCESS_DENIED);
    } catch (MalformedJwtException e) {
      throw new UserException(JWT_ACCESS_DENIED);
    } catch (SignatureException e) {
      throw new UserException(JWT_ACCESS_DENIED);
    } catch (IllegalArgumentException e) {
      throw new UserException(JWT_ACCESS_DENIED);
    }
  }

  public String extractAccessTokenFromCookies(String cookies) {
    // 쿠키 문자열에서 원하는 쿠키 (예: "your_cookie_name")의 값을 추출
    if (!ObjectUtils.isEmpty(cookies)) {
      String[] cookiePairs = cookies.split("; ");
      for (String cookiePair : cookiePairs) {
        if (cookiePair.startsWith("accessToken=")) {
          return cookiePair.substring("accessToken=".length());
        }
      }
    }
    return null;
  }

  public String extractRefreshTokenFromCookies(String cookies) {
    if (!ObjectUtils.isEmpty(cookies)) {
      String[] cookiePairs = cookies.split("; ");
      for (String cookiePair : cookiePairs) {
        if (cookiePair.startsWith("refreshToken=")) {
          return cookiePair.substring("refreshToken=".length());
        }
      }
    }
    return null;
  }


}
