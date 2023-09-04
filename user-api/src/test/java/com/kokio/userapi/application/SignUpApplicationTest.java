package com.kokio.userapi.application;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

import com.kokio.entitymodule.domain.user.entity.User;
import com.kokio.entitymodule.domain.user.model.Sign;
import com.kokio.entitymodule.domain.user.model.Sign.In;
import com.kokio.entitymodule.domain.user.model.Sign.Up;
import com.kokio.userapi.client.MailgunClient;
import com.kokio.userapi.service.author.AuthorSignUpService;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest
@AutoConfigureTestDatabase
@ActiveProfiles("test")
class SignUpApplicationTest {

  @Autowired
  private SignUpApplication signUpApplication;

  @MockBean
  private AuthorSignUpService authorSignUpService;

  @MockBean
  private MailgunClient mailgunClient;


  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }


  @Test
  public void userSignUpTest() throws Exception {
    //given
    Sign.Up up = signUpForm();
    User user = up.toUser();

    //when
    when(authorSignUpService.checkExistEmail(anyString())).thenReturn(false);
    when(authorSignUpService.register(up)).thenReturn(user);
    when(authorSignUpService.getRandomCode()).thenReturn("randomCode");

    ResponseEntity<String> mailResponse = ResponseEntity.ok("success");
    when(mailgunClient.sendMail(any())).thenReturn(mailResponse);

    String resultMessage = signUpApplication.userSignUp(up); //실제 데이터

    //Then
    assertTrue(resultMessage.contains("stringstring"));

  }

  private static Sign.Up signUpForm() {
    List<String> list = new ArrayList<>();
    list.add("ROLE_CUSTOMER");
    return Up.builder().email("abc@naver.com").password("string").firstname("string")
        .lastName("string").roles(list).build();
  }


  private static Sign.In signInForm() {
    return new In("hyomyang@naver.com", "string");
  }


}