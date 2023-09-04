package com.kokio.productapi.utill;


import com.kokio.entitymodule.domain.user.model.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "user-api", url = "${feign.client.url.user-api}")
public interface UserUtility {


  @GetMapping("/auth/getInfo/module")
  ResponseEntity<UserDto> getUserInfoForModule(
      @RequestHeader(name = "Authorization") String token);
}
