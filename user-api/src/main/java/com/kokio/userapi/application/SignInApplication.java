package com.kokio.userapi.application;


import static com.kokio.commonmodule.exception.Code.UserErrorCode.PASSWORD_NOT_MATCHED;
import static com.kokio.commonmodule.exception.Code.UserErrorCode.USER_NOT_FOUND;

import com.kokio.commonmodule.exception.UserException;
import com.kokio.commonmodule.security.TokenProvider;
import com.kokio.commonmodule.security.common.UserVo;
import com.kokio.entitymodule.domain.user.entity.User;
import com.kokio.entitymodule.domain.user.model.Sign.In;
import com.kokio.userapi.model.security.Token;
import com.kokio.userapi.service.author.AuthorSignInService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignInApplication {

  private final AuthorSignInService authorSignInService;
  private final TokenProvider provider;

  private final PasswordEncoder passwordEncoder;


  public Token userTokenLogin(In form) {
    User user = authorSignInService.validUserFind(form.getEmail()).orElseThrow(
        () -> new UserException(USER_NOT_FOUND)
    );
    if (!passwordEncoder.matches(form.getPassword(), user.getPassword())) {
      throw new UserException(PASSWORD_NOT_MATCHED);
    }
    Token tokens = new Token(provider.createToken(user.getEmail(), user.getId(), user.getRoles()),
        provider.createRefreshToken(user.getEmail(), user.getId(), user.getRoles()));

    return tokens;

  }

  public Authentication getAuthentication(String token) {
    UserVo userVo = provider.getUserVo(token);
    UserDetails userDetails = authorSignInService.loadUserByUsername(userVo.getEmail());
    return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());

  }


}
