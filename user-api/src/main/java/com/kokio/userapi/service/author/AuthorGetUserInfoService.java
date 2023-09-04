package com.kokio.userapi.service.author;

import com.kokio.userapi.domain.entity.User;
import com.kokio.userapi.domain.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorGetUserInfoService {
  private final UserRepository userRepository;


  public Optional<User> getUserInfo(Long userId, String email){
    return userRepository.findById(userId).stream().filter(user -> user.getEmail().equals(email)).findFirst();
  }



}
