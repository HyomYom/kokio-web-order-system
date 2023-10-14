package com.kokio.entitymodule.domain.user.entity;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "chatMessages")
@AllArgsConstructor
public class ChatMessage {


  @Id
  private String id;
  private Map<String, Object> message;
  private LocalDateTime createdAt;

  public ChatMessage() {

    this.message = new HashMap<>();
    this.createdAt = LocalDateTime.now();
  }
}