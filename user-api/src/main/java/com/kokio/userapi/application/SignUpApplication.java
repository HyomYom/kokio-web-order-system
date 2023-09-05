package com.kokio.userapi.application;


import static com.kokio.commonmodule.exception.Code.UserErrorCode.ALREADY_VERIFY;
import static com.kokio.commonmodule.exception.Code.UserErrorCode.FAIL_TO_SEND_EMAIL;
import static com.kokio.commonmodule.exception.Code.UserErrorCode.USER_ALREADY_EXIST;
import static com.kokio.commonmodule.exception.Code.UserErrorCode.USER_NOT_FOUND;
import static com.kokio.commonmodule.exception.Code.UserErrorCode.VERIFICATION_PERIOD_HAS_EXPIRED;
import static com.kokio.commonmodule.exception.Code.UserErrorCode.WRONG_VERIFICATION_CODE;

import com.kokio.commonmodule.exception.UserException;
import com.kokio.entitymodule.domain.user.entity.User;
import com.kokio.entitymodule.domain.user.model.Sign;
import com.kokio.userapi.client.MailgunClient;
import com.kokio.userapi.client.form.SendMailForm;
import com.kokio.userapi.service.author.AuthorSignUpService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Getter
@Slf4j
public class SignUpApplication {

  private final AuthorSignUpService authorSignUpService;
  private final MailgunClient mailgunClient;

  @Value("${spring.mailgun.author}")
  private String authorEmail;

  public String userSignUp(Sign.Up form) {
    if (authorSignUpService.checkExistEmail(form.getEmail())) {
      throw new UserException(USER_ALREADY_EXIST);
    }
    User register = authorSignUpService.register(form);
    String randomCode = authorSignUpService.getRandomCode();
    String fullName = form.getLastName() + form.getFirstname();

    try {
      ResponseEntity<String> verificationEmail = mailgunClient.sendMail(SendMailForm.builder()
          .from(authorEmail)
          .to(form.getEmail())
          .subject("Verification Email")
          .text(getVerificationEmailBody(form.getEmail(), fullName, randomCode))
          .build());

      log.info("Send Email Result : " + verificationEmail);
    } catch (UserException e) {
      throw new UserException(FAIL_TO_SEND_EMAIL);
    }

    LocalDate expiredAt = authorSignUpService.setUserPermissionInfo(register.getId(),
        register.getEmail(), randomCode);

    return fullName + "님, 회원 가입에 성공하였습니다. \n" + String.valueOf(expiredAt) + " 이전까지 이메일 인증을 완료해주세요.";
  }

  @Transactional
  public String userVerify(String email, String verifyCode) {

    User user = authorSignUpService.userVerify(email, verifyCode).orElseThrow(
        () -> new UserException(USER_NOT_FOUND)
    );

    if (user.isVerify()) {
      throw new UserException(ALREADY_VERIFY);
    } else if (!user.getVerificationCode().equals(verifyCode)) {
      throw new UserException(WRONG_VERIFICATION_CODE);
    } else if (user.getVerifyExpiredAt().isBefore(LocalDateTime.now())) {
      throw new UserException(VERIFICATION_PERIOD_HAS_EXPIRED);
    }
    user.setVerify(true);

    return "인증에 성공하였습니다.";
  }

  private String getVerificationEmailBody(String email, String name, String code) {
    StringBuilder sb = new StringBuilder();
    return sb.append("안녕하세요! ").append(name + "님").append(" 확인을 위해 아래 링크를 클릭하여 주세요.\n\n")
        .append("http://localhost:8081/auth/register/verify?email=")
        .append(email)
        .append("&code=")
        .append(code).toString();
  }


}
