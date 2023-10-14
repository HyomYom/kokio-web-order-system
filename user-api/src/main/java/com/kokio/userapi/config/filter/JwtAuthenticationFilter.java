package com.kokio.userapi.config.filter;


import static com.kokio.commonmodule.exception.Code.UserErrorCode.JWT_REFRESH_TOKEN_EXPIRED;
import static com.kokio.commonmodule.exception.Code.UserErrorCode.JWT_TOKEN_EXPIRED;

import com.kokio.commonmodule.exception.UserException;
import com.kokio.commonmodule.security.TokenProvider;
import com.kokio.userapi.application.SignInApplication;
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
    String accessToken = provider.extractAccessTokenFromCookies(request.getHeader("Cookie"));
    String refreshToken = provider.extractRefreshTokenFromCookies(request.getHeader("Cookie"));
    String uri = request.getRequestURI();
    if (!uri.startsWith("/chat/login") && !uri.startsWith("/auth/login") && !uri.startsWith("/swagger-ui") && !uri.startsWith("/v2/api-docs")) {
      if(uri.startsWith("/auth/refresh_token")) accessToken = refreshToken;
      if (!ObjectUtils.isEmpty(accessToken)) {
        try {
          if (provider.validateToken(accessToken)) {
            Authentication authentication = signInApplication.getAuthentication(accessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
          }
        } catch (UserException e) {
          System.out.println(refreshToken);
          if (!ObjectUtils.isEmpty(refreshToken) && provider.validateToken(refreshToken)) {
            if (uri.startsWith("/chat/room")) {
              response.sendRedirect("/chat/login");
              return;
            } else {
              throw new UserException(JWT_TOKEN_EXPIRED);
            }
          } else {
            throw new UserException(JWT_REFRESH_TOKEN_EXPIRED);
          }
        }
      }
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
