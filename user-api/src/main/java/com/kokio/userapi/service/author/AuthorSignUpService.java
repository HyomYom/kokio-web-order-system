package com.kokio.userapi.service.author;


import static com.kokio.commonmodule.exception.Code.UserErrorCode.USER_NOT_FOUND;

import com.kokio.commonmodule.exception.UserException;
import com.kokio.entitymodule.domain.user.entity.User;
import com.kokio.entitymodule.domain.user.model.Sign;
import com.kokio.entitymodule.domain.user.repository.UserRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthorSignUpService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;


  @Transactional
  public User register(Sign.Up form) {
    boolean exists = this.userRepository.existsByEmail(form.getEmail());

    form.setPassword(this.passwordEncoder.encode(form.getPassword()));

    return this.userRepository.save(form.toUser());
  }

  @Transactional
  public boolean checkExistEmail(String email) {
    return userRepository.existsByEmail(email);
  }

  @Transactional
  public LocalDate setUserPermissionInfo(Long userId, String email, String verificationCode) {
    Optional<User> optionalUser = userRepository.findByIdAndEmail(userId, email);
    if (optionalUser.isPresent()) {
      User user = optionalUser.get();
      user.setVerificationCode(verificationCode);
      user.setVerifyExpiredAt(LocalDateTime.now().plusDays(1));
      return user.getVerifyExpiredAt().toLocalDate();
    }

    throw new UserException(USER_NOT_FOUND);
  }


  @Transactional
  public Optional<User> userVerify(String email, String verifyCode) {
    return userRepository.findByEmail(email);

  }

  public String getRandomCode() {
    return RandomStringUtils.random(26, true, true);
  }


}
