package com.kokio.userapi.service.author;


import static com.kokio.commonmodule.exception.Code.UserErrorCode.USER_NOT_FOUND;

import com.kokio.commonmodule.exception.UserException;
import com.kokio.entitymodule.domain.user.entity.User;
import com.kokio.entitymodule.domain.user.repository.UserRepository;
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
        .orElseThrow(() -> new UserException(USER_NOT_FOUND));
  }

  public Optional<User> validUserFind(String email) {
    return userRepository.findByEmailAndVerifyIsTrue(email);
  }


}
