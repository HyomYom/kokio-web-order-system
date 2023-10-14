package com.kokio.userapi.model.room;


import com.kokio.userapi.model.chat.SendChatMessageForm;
import com.kokio.userapi.model.chat.SendChatMessageForm.MessageType;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessageDto {


  private MessageType type;
  private String content;
  private String sender;
  private String roomId;
  private LocalDateTime createdAt;

  public SendChatMessageForm toChatMessageForm() {
    return SendChatMessageForm.builder()
        .type(this.type)
        .content(this.content)
        .sender(this.sender)
        .roomId(this.roomId)
        .build();

  }

}
