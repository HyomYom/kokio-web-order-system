package com.kokio.userapi.service.author;

import static com.kokio.userapi.exception.ErrorCode.ALREADY_VERIFY;
import static com.kokio.userapi.exception.ErrorCode.USER_NOT_FOUND;
import static com.kokio.userapi.exception.ErrorCode.VERIFICATION_PERIOD_HAS_EXPIRED;
import static com.kokio.userapi.exception.ErrorCode.WRONG_VERIFICATION_CODE;

import com.kokio.userapi.domain.entity.User;
import com.kokio.userapi.domain.model.Sign;
import com.kokio.userapi.domain.repository.UserRepository;
import com.kokio.userapi.exception.CustomException;
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

    throw new CustomException(USER_NOT_FOUND);
  }


  @Transactional
  public void userVerify(String email, String verifyCode) {
    User user = userRepository.findByEmail(email).orElseThrow(
        () -> new CustomException(USER_NOT_FOUND)
    );
    if (user.isVerify()) {
      throw new CustomException(ALREADY_VERIFY);
    } else if (!user.getVerificationCode().equals(verifyCode)) {
      throw new CustomException(WRONG_VERIFICATION_CODE);
    } else if (user.getVerifyExpiredAt().isBefore(LocalDateTime.now())) {
      throw new CustomException(VERIFICATION_PERIOD_HAS_EXPIRED);
    }
    user.setVerify(true);
  }

  public String getRandomCode() {
    return RandomStringUtils.random(26, true, true);
  }


}
