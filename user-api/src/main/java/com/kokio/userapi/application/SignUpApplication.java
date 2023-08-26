package com.kokio.userapi.application;


import static com.kokio.userapi.exception.ErrorCode.FAIL_TO_SEND_EMAIL;
import static com.kokio.userapi.exception.ErrorCode.USER_ALREADY_EXIST;

import com.kokio.userapi.client.MailgunClient;
import com.kokio.userapi.client.form.SendMailForm;
import com.kokio.userapi.domain.entity.User;
import com.kokio.userapi.domain.model.Sign;
import com.kokio.userapi.exception.CustomException;
import com.kokio.userapi.service.author.AuthorSignUpService;
import java.time.LocalDate;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
      throw new CustomException(USER_ALREADY_EXIST);
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
    } catch (Exception e) {
      throw new CustomException(FAIL_TO_SEND_EMAIL);
    }

    LocalDate expiredAt = authorSignUpService.setUserPermissionInfo(register.getId(),
        register.getEmail(), randomCode);

    return fullName + "님, 회원 가입에 성공하였습니다. \n" + String.valueOf(expiredAt) + " 이전까지 이메일 인증을 완료해주세요.";
  }

  public String userVerify(String email, String verifyCode) {

    authorSignUpService.userVerify(email, verifyCode);
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
