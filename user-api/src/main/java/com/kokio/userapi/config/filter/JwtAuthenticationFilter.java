package com.kokio.userapi.config.filter;


import static com.kokio.commonmodule.exception.Code.UserErrorCode.JWT_TOKEN_EXPIRED;

import com.kokio.commonmodule.exception.UserException;
import com.kokio.commonmodule.security.TokenProvider;
import com.kokio.userapi.application.SignInApplication;
import io.jsonwebtoken.ExpiredJwtException;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  public static final String TOKEN_HEADER = "Authorization";
  public static final String TOKEN_PREFIX = "Bearer ";

  private final TokenProvider provider;
  private final SignInApplication signInApplication;


  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws UserException, ServletException, IOException {
    String requestUrl = request.getRequestURI();

    try {
      if (!requestUrl.equals("/auth/register") && !requestUrl.equals("/auth/login")
          && !requestUrl.equals("/auth/register/verify")) {

        String token = resolveTokenFromRequest(request);

        if (!ObjectUtils.isEmpty(token) && provider.validateToken(token)) {
          Authentication authentication = signInApplication.getAuthentication(token);
          SecurityContextHolder.getContext().setAuthentication(authentication);

          filterChain.doFilter(request, response);
          return;
        }
      }
    } catch (ExpiredJwtException e) {
      throw new UserException(JWT_TOKEN_EXPIRED);
    }

    filterChain.doFilter(request, response);

  }

  private String resolveTokenFromRequest(HttpServletRequest request) {
    String token = request.getHeader(TOKEN_HEADER);
    if (!ObjectUtils.isEmpty(token) && token.startsWith(TOKEN_PREFIX)) {
      return token.substring(TOKEN_PREFIX.length());
    }
    return null;
  }
}
