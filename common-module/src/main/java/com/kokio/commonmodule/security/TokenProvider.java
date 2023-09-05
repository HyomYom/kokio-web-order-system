package com.kokio.commonmodule.security;


import com.kokio.commonmodule.security.common.UserVo;
import com.kokio.commonmodule.utill.SecureAes256Util;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;


@Component
@RequiredArgsConstructor
@Slf4j
public class
TokenProvider {

  private static final long TOKEN_EXPIRE_TIME = 1000 * 60 * 60;
  private static final String KEY_ROLES = "roles";

  @Value("${spring.jwt.secret}")
  private String secretKey;

//  @Value("${spring.util.secret}")
//  private String aesKey;

  private String aesKey = generateFixedSizeString(32, secretKey);

  public String createToken(String userPk, Long id, List<String> roles) {
    Claims claims = Jwts.claims().setSubject(SecureAes256Util.encrypt(aesKey, userPk))
        .setId(SecureAes256Util.encrypt(aesKey, String.valueOf(id)));

    claims.put(KEY_ROLES, roles);
    Date now = new Date();
    return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(now)
        .setExpiration(new Date(now.getTime() + TOKEN_EXPIRE_TIME))
        .signWith(SignatureAlgorithm.HS256, this.secretKey)
        .compact();
  }

  public boolean validateToken(String token) throws ExpiredJwtException {
    if (!StringUtils.hasText(token)) {
      return false;
    }

    Claims claims = claimParser(token);
    return !claims.getExpiration().before(new Date());
  }

  public UserVo getUserVo(String token) {
    log.info(token);
    Claims claims = claimParser(token);
    log.info(SecureAes256Util.decrypt(aesKey, claims.getSubject()));
    return new UserVo(Long.valueOf(SecureAes256Util.decrypt(aesKey, claims.getId())),
        SecureAes256Util.decrypt(aesKey, claims.getSubject()), (List<String>) claims.get(KEY_ROLES)
    );
  }

  public Claims claimParser(String token) throws ExpiredJwtException {
    return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token)
        .getBody();
  }

  public String generateFixedSizeString(int size, String key) {
    StringBuilder sb = new StringBuilder();
    while (sb.toString().getBytes().length < size) {
      sb.append("a");
    }
    return sb.toString().substring(0, size);

  }


}
