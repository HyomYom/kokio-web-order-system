package com.kokio.userapi.client.form;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SendMailForm {

  private String from;
  private String to;
  private String subject;
  private String text;

}