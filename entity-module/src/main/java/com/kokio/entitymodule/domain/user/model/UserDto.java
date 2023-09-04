package com.kokio.entitymodule.domain.user.model;

import com.kokio.entitymodule.domain.user.entity.User;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserDto {

  private Long id;
  private String email;
  private String firstName;
  private String lastName;
  private List<String> roles;

  public static UserDto toDto(User user) {

    return UserDto.builder()
        .id(user.getId())
        .email(user.getEmail())
        .firstName(user.getFirstname())
        .lastName(user.getLastName())
        .roles(user.getRoles())
        .build();


  }


}
