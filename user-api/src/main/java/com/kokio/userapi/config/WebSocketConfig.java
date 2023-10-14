package com.kokio.userapi.config;

import com.kokio.userapi.config.filter.ChatErrorHandler;
import com.kokio.userapi.config.filter.HttpHandshakeInterceptor;
import com.kokio.userapi.config.filter.StompInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@RequiredArgsConstructor
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

  private final StompInterceptor stompInterceptor;
  private final ChatErrorHandler chatErrorHandler;
  private final HttpHandshakeInterceptor httpHandshakeInterceptor;


  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry.addEndpoint("/ws/chat").setAllowedOriginPatterns("*")
        .addInterceptors(httpHandshakeInterceptor).withSockJS();
    registry.setErrorHandler(chatErrorHandler);

  }

  @Override
  public void configureMessageBroker(MessageBrokerRegistry registry) {

    registry.enableSimpleBroker("/queue", "/sub");

    registry.setApplicationDestinationPrefixes("/pub");
  }
//  @Override
//  public void configureClientInboundChannel(ChannelRegistration registration) {
//    registration.interceptors(stompInterceptor);
//  }


}