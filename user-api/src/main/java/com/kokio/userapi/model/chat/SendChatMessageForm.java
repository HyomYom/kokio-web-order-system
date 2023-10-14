package com.kokio.userapi.model.chat;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendChatMessageForm {

  public enum MessageType {
    ENTER, TALK, LEAVE
  }

  private SendChatMessageForm.MessageType type;
  //채팅방 ID
  private String content;
  private String sender; //발신
  private String roomId; //수신
  private LocalDateTime createdAt;

  public SendChatMessageForm(SendChatMessageForm form) {
    this.type = form.type;
    this.content = form.content;
    this.sender = form.sender;
    this.roomId = form.roomId;
    this.createdAt = LocalDateTime.now();
  }


}
