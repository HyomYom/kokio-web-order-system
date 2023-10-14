package com.kokio.entitymodule.domain.user.entity;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "chatRooms")
public class ChatRoom implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  private String roomId;
  private String roomName;
  private List<String> users;


  public static ChatRoom create(String name) {
    ChatRoom room = new ChatRoom();
    room.roomId = UUID.randomUUID().toString();
    room.roomName = name;
    return room;
  }
}