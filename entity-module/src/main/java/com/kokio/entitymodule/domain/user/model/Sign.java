package com.kokio.entitymodule.domain.user.model;

import com.kokio.entitymodule.domain.user.entity.User;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;


public class Sign {

  @Data
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class Up {

    private String email;
    private String password;
    private String lastName;
    private String firstname;
    private LocalDate birth;
    private List<String> roles;
    private String phone;

    public User toUser() {
      return User.builder()
          .email(this.email)
          .password(this.password)
          .lastName(this.lastName)
          .firstname(this.firstname)
          .birth(this.birth)
          .roles(this.roles)
          .phone(this.phone)
          .build();
    }
  }

  @Getter
  @AllArgsConstructor
  @NoArgsConstructor
  public static class In {

    String email;
    String password;
  }


}
