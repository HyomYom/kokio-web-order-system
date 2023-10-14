package com.kokio.userapi.config.filter;

import static com.kokio.commonmodule.exception.Code.UserErrorCode.JWT_ACCESS_DENIED;

import com.kokio.commonmodule.exception.Code.UserErrorCode;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;

@Component
@Slf4j
public class ChatErrorHandler extends StompSubProtocolErrorHandler {

  @Override
  public Message<byte[]> handleClientMessageProcessingError(Message<byte[]> clientMessage,
      Throwable ex) {
    log.info("시작");

    if (ex.getCause().getMessage().equals("JWT")) {
      return handleJwtException(clientMessage, ex);
    }
    return super.handleClientMessageProcessingError(clientMessage, ex);
  }

  private Message<byte[]> handleJwtException(Message<byte[]> clientMessage, Throwable ex) {
    return prepareErrorMessage(JWT_ACCESS_DENIED);
  }

  private Message<byte[]> prepareErrorMessage(UserErrorCode responseCode) {
    String code = String.valueOf(responseCode.getDetail());

    StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.ERROR);
    accessor.setMessage(String.valueOf(responseCode.getHttpStatus()));
    accessor.setLeaveMutable(true);
    return MessageBuilder.createMessage(code.getBytes(StandardCharsets.UTF_8),
        accessor.getMessageHeaders());
  }
}
