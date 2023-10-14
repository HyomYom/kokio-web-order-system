package com.kokio.userapi.service.message;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kokio.entitymodule.domain.user.entity.ChatMessage;
import com.kokio.entitymodule.domain.user.entity.ChatRoom;
import com.kokio.entitymodule.domain.user.repository.ChatMessageRepository;
import com.kokio.entitymodule.domain.user.repository.ChatRoomRepository;
import com.kokio.userapi.model.chat.ReceiveChatMessageForm;
import com.kokio.userapi.model.chat.SendChatMessageForm;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageSubscriberService implements MessageListener {

  private final ChatMessageRepository chatMessageRepository;
  private final ChatRoomRepository chatRoomRepository;
  private final SimpMessagingTemplate simpMessagingTemplate;
  private final ObjectMapper objectMapper;
  private final String WS_CHAT_PATTERN = "/sub/chat/room/";

  /**
   * @param message message must not be {@literal null}.
   * @param pattern pattern matching the channel (if specified) - can be {@literal null}.
   */
  @Override
  public void onMessage(Message message, byte[] pattern) {

    String channel = new String(message.getChannel());
    String payload = new String(message.getBody());

    if (channel.startsWith("/chat/room/")) {
      handleChatMessage(payload);
    } else {
      throw new RuntimeException();
    }
  }

  private void handleChatMessage(String payload) {
    log.info("handleChatMessage!");
    try {
      SendChatMessageForm sendChatMessageForm = getChatMessageForm(payload);
      ChatMessage chatMessage = createChatMessageFromForm(sendChatMessageForm);
      chatMessageRepository.save(chatMessage);

      ReceiveChatMessageForm receiveChatMessageForm = new ReceiveChatMessageForm(
          objectMapper.convertValue(sendChatMessageForm, ReceiveChatMessageForm.class),
          sendChatMessageForm.getCreatedAt());

      simpMessagingTemplate.convertAndSend("/sub/chat/room/" + receiveChatMessageForm.getRoomId(),
          receiveChatMessageForm);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }

  }

  private SendChatMessageForm getChatMessageForm(String payload) throws JsonProcessingException {
    return new SendChatMessageForm(objectMapper.readValue(payload, SendChatMessageForm.class));
  }

  /**
   * @param roomId
   * @param userId
   * @return
   */
  public List<ReceiveChatMessageForm> getMessageList(String roomId, String userId) {
    ChatRoom chatRoom = chatRoomRepository.findById(roomId)
        .orElseThrow(() -> new RuntimeException());
    if (chatRoom.getUsers().contains(userId)) {
      return chatMessageRepository.findByRoomIdInMessage(roomId).stream().map(chatMessage -> {

        ReceiveChatMessageForm receiveChatMessageForm = new ReceiveChatMessageForm(
            objectMapper.convertValue(
                chatMessage.getMessage(),
                ReceiveChatMessageForm.class), chatMessage.getCreatedAt());

        return receiveChatMessageForm;
      }).collect(Collectors.toList());
    }
    return null;
  }


  private ChatMessage createChatMessageFromForm(SendChatMessageForm form) {
    Map<String, Object> messageInfo = objectMapper.convertValue(form,
        new TypeReference<Map<String, Object>>() {
        });
    ChatMessage chatMessage = new ChatMessage();
    chatMessage.getMessage().putAll(messageInfo);

    return chatMessage;
  }


}
