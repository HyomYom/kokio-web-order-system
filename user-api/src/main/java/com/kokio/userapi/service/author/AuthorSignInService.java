package com.kokio.userapi.service.author;

import static com.kokio.userapi.exception.ErrorCode.USER_NOT_FOUND;

import com.kokio.userapi.domain.entity.User;
import com.kokio.userapi.domain.repository.UserRepository;
import com.kokio.userapi.exception.CustomException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorSignInService implements UserDetailsService {

  private final UserRepository userRepository;


  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

    return this.userRepository.findByEmail(email)
        .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
  }

  public Optional<User> validUserFind(String email) {
    return userRepository.findByEmailAndVerifyIsTrue(email);
  }


}
