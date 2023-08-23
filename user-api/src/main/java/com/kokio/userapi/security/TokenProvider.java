package com.kokio.userapi.security;

import com.kokio.userapi.service.AuthorService;
import com.kokio.userapi.utill.SecureAes256Util;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.usertype.UserType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;


@Component
@RequiredArgsConstructor
public class TokenProvider {

  private final AuthorService authorService;
  private static final long TOKEN_EXPIRE_TIME = 1000 * 60 * 60;
  private static final String KEY_ROLES = "roles";

  @Value("${spring.jwt.secret}")
  private String secretKey;

  public String createToken(String userPk, Long id, List<String> roles) {
    Claims claims = Jwts.claims().setSubject(SecureAes256Util.encrypt(userPk))
        .setId(SecureAes256Util.encrypt(String.valueOf(id)));

    claims.put(KEY_ROLES, roles);
    Date now = new Date();
    return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(now)
        .setExpiration(new Date(now.getTime() + TOKEN_EXPIRE_TIME))
        .signWith(SignatureAlgorithm.HS512, this.secretKey)
        .compact();
  }

  public boolean validateToken(String token){
    if(!StringUtils.hasText(token)) return false;
    Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
    return !claimsJws.getBody().getExpiration().before(new Date());
  }

//


}
