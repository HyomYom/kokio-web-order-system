package com.kokio.userapi.application;

import static com.kokio.userapi.exception.ErrorCode.PASSWORD_NOT_MATCHED;
import static com.kokio.userapi.exception.ErrorCode.USER_NOT_FOUND;

import com.kokio.tokenprovider.security.TokenProvider;
import com.kokio.tokenprovider.security.common.UserVo;
import com.kokio.userapi.domain.entity.User;
import com.kokio.userapi.domain.model.Sign;
import com.kokio.userapi.exception.CustomException;
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


  public String userTokenLogin(Sign.In form) {
    User user = authorSignInService.validUserFind(form.getEmail()).orElseThrow(
        () -> new CustomException(USER_NOT_FOUND)
    );
    if (!passwordEncoder.matches(form.getPassword(), user.getPassword())) {
      throw new CustomException(PASSWORD_NOT_MATCHED);
    }
    return provider.createToken(user.getEmail(), user.getId(), user.getRoles());

  }

  public Authentication getAuthentication(String token) {
    UserVo userVo = provider.getUserVo(token);
    UserDetails userDetails = authorSignInService.loadUserByUsername(userVo.getEmail());
    return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());

  }


}
