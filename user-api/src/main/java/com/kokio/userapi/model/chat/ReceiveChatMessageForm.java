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
public class ReceiveChatMessageForm {

  public enum MessageType {
    ENTER, TALK, LEAVE
  }

  private ReceiveChatMessageForm.MessageType type;
  //채팅방 ID
  private String content;
  private String sender; //발신
  private String roomId; //수신
  private LocalDateTime createdAt;

  private String year;
  private String month;
  private String day;
  private String hour;
  private String minute;

  public ReceiveChatMessageForm(ReceiveChatMessageForm form,
      LocalDateTime createdAt) {
    this.type = form.getType();
    this.content = form.content;
    this.sender = form.sender;
    this.roomId = form.roomId;
    this.createdAt = createdAt;
    this.year = String.format("%02d", createdAt.getYear());
    this.month = String.format("%02d", createdAt.getMonthValue());
    this.day = String.format("%02d", createdAt.getDayOfMonth());
    this.hour = String.format("%02d", createdAt.getHour());
    this.minute = String.format("%02d", createdAt.getMinute());
  }

  public ReceiveChatMessageForm rebuild(ReceiveChatMessageForm form, LocalDateTime createdAt) {
    return ReceiveChatMessageForm.builder()
        .content(form.content)
        .sender(form.sender)
        .roomId(form.roomId)
        .createdAt(form.createdAt)
        .year(String.format("%02d", createdAt.getYear()))
        .month(String.format("%02d", createdAt.getMonthValue()))
        .day(String.format("%02d", createdAt.getDayOfMonth()))
        .hour(String.format("%02d", createdAt.getHour()))
        .minute(String.format("%02d", createdAt.getMinute()))
        .build();
  }
}
