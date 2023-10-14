package com.kokio.userapi.config.filter;

import static com.kokio.commonmodule.exception.Code.UserErrorCode.JWT_ACCESS_DENIED;

import com.kokio.commonmodule.exception.UserException;
import com.kokio.commonmodule.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.stereotype.Component;


@Component
@Slf4j
@RequiredArgsConstructor
public class StompInterceptor extends ChannelInterceptorAdapter {

  private final TokenProvider provider;

  @Override
  public Message<?> preSend(Message<?> message, MessageChannel channel) {
    StompHeaderAccessor accessor = StompHeaderAccessor.getAccessor(message,
        StompHeaderAccessor.class);
    try {
      log.info("여기는 채널 intercep");
      if (accessor != null && accessor.getCommand() == StompCommand.CONNECT) {
        if (!provider.validateToken(accessor.getFirstNativeHeader("token"))) {
          throw new UserException(JWT_ACCESS_DENIED);
        }
      }
    } catch (UserException e) {
      throw new UserException(JWT_ACCESS_DENIED, "JWT");
    }
    return message;
  }
}
