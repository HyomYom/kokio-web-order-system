package com.kokio.userapi.service;

import static com.kokio.userapi.exception.ErrorCode.USER_ALREADY_EXIST;
import static com.kokio.userapi.exception.ErrorCode.USER_NOT_FOUND;

import com.kokio.userapi.domain.entity.User;
import com.kokio.userapi.domain.model.Sign;
import com.kokio.userapi.domain.repository.UserRepository;
import com.kokio.userapi.exception.CustomException;
import com.kokio.userapi.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorService implements UserDetailsService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;


  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

    return this.userRepository.findByEmail(email)
        .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
  }

  public User register(Sign.Up form) {
    boolean exists = this.userRepository.existsByEmail(form.getEmail());
    if (exists) {
      throw new CustomException(USER_ALREADY_EXIST);
    }
    form.setPassword(this.passwordEncoder.encode(form.getPassword()));
    var result = this.userRepository.save(form.toUser());
    return result;
  }
}
