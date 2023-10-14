package com.kokio.userapi.config.filter;

import com.kokio.commonmodule.security.TokenProvider;
import java.util.Map;
import javax.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;


@Slf4j
@Component
@RequiredArgsConstructor
public class HttpHandshakeInterceptor implements HandshakeInterceptor {

  private final TokenProvider provider;

  @Override
  public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
      WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {

    if (request instanceof ServletServerHttpRequest) {
      ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
      String token = null;
      Cookie[] cookies = servletRequest.getServletRequest().getCookies();

      if (!ObjectUtils.isEmpty(cookies)) {
        for (Cookie cookie : cookies) {
          if (cookie.getName().equals("accessToken")) {
            if (!provider.validateToken(cookie.getValue())) {
              return false;
            }
          }

        }
      }
    }

    return true;
  }


  @Override
  public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
      WebSocketHandler wsHandler, Exception exception) {
  }
}
