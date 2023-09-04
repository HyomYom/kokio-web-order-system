package com.kokio.userapi.client;


import com.kokio.userapi.client.form.SendMailForm;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "mailgun", url="https://api.mailgun.net/v3/")
public interface MailgunClient {
  @PostMapping("${spring.mailgun.domain}")
  ResponseEntity<String> sendMail(@SpringQueryMap SendMailForm form);

}
