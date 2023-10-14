package com.kokio.productapi.client;


import com.kokio.entitymodule.domain.user.model.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "user-api", url = "${feign.client.url.user-api}")
public interface UserClient {


  @GetMapping("/auth/getInfo/module")
  ResponseEntity<UserDto> getUserInfoForModule(
      @CookieValue(name = "accessToken") String token);
}
