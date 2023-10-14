package com.kokio.userapi.service.message;

import com.kokio.entitymodule.domain.user.entity.ChatRoom;
import com.kokio.entitymodule.domain.user.repository.ChatRoomRepository;
import com.kokio.userapi.model.room.ChatRoomDto;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatRoomService {

  private final ChatRoomRepository chatRoomRepository;

  //채팅방 참가
  public void userJoinRoom(String userId, String roomId) {
    ChatRoom chatRoom = chatRoomRepository.findById(roomId)
        .orElseThrow(() -> new RuntimeException());
    if (chatRoom.getUsers().stream().noneMatch(user -> user.equals(userId))) {
      chatRoom.getUsers().add(userId);
    } else {
      throw new RuntimeException();
    }

    chatRoomRepository.save(chatRoom);
  }

  //특정 채팅방 불러오기
  public ChatRoom findRoom(String roomId) {
    return chatRoomRepository.findById(roomId).orElseThrow(() -> new RuntimeException());
  }


  //모든 채팅방 불러오기
  public List<ChatRoom> findAllRoom(String userId) {
    //채팅방 최근 생성 순으로 반환
    List<ChatRoom> result = chatRoomRepository.findByUsersContains(userId);
    Collections.reverse(result);

    return result;
  }

  //채팅방 생성
  public ChatRoom createRoom(ChatRoomDto chatRoomDto) {
    ChatRoom chatRoom = chatRoomDto.toChatRoom();
    return chatRoomRepository.save(chatRoom);
  }
}
