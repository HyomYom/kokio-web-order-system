package com.kokio.commonmodule.security.common;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserVo {

  private Long id;
  private String email;
  private List<String> roles;

}
