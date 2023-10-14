package com.kokio.userapi.model.room;

import com.kokio.entitymodule.domain.user.entity.ChatRoom;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomDto {

  private String userId;
  private String roomName;


  public ChatRoom toChatRoom() {
    List<String> users = new ArrayList<>();
    users.add(this.userId);
    return ChatRoom.builder()
        .roomName(this.roomName)
        .users(users)
        .build();
  }

}
