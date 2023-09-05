package com.kokio.userapi.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kokio.commonmodule.security.TokenProvider;
import com.kokio.commonmodule.security.common.UserVo;
import com.kokio.entitymodule.domain.user.entity.User;
import com.kokio.entitymodule.domain.user.model.Sign;
import com.kokio.entitymodule.domain.user.model.Sign.In;
import com.kokio.entitymodule.domain.user.model.Sign.Up;
import com.kokio.userapi.application.SignInApplication;
import com.kokio.userapi.application.SignUpApplication;
import com.kokio.userapi.service.author.AuthorGetUserInfoService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

  @InjectMocks
  private AuthController authController;

  private MockMvc mockMvc;
  @Mock
  private SignUpApplication signUpApplication;

  @Mock
  private SignInApplication signInApplication;

  @Mock
  private AuthorGetUserInfoService authorGetUserInfoService;

  @Mock
  private TokenProvider tokenProvider;

  private final ObjectMapper objectMapper = new ObjectMapper();

  @BeforeEach
  public void setup() {
    mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
  }

  @Test
  public void testUserSignUp() throws Exception {
    Sign.Up up = signUpForm();

    when(signUpApplication.userSignUp(any(Sign.Up.class))).thenReturn("success");

    mockMvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(up))).andExpect(status().isOk())
        .andExpect(content().string("success"));
  }

  @Test
  public void testUserSignIn() throws Exception {

    Sign.In in = signInForm();

    when(signInApplication.userTokenLogin(any(Sign.In.class))).thenReturn("token");

    mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(in))).andExpect(status().isOk())
        .andExpect(content().string("token"));
  }

  @Test
  public void testSignUpVerify() throws Exception {
    String email = "abcd@naver.com";
    String code = "ABCDEFG";

    when(signUpApplication.userVerify(anyString(), anyString())).thenReturn("success");

    mockMvc.perform(get("/auth/register/verify?email=" + email + "&code=" + code))
        .andExpect(status().isOk()).andExpect(content().string("success"));
  }

  @Test
  @PreAuthorize("hasRole('CUSTOEMR')")
  public void testGetInfo() throws Exception {
    String token = "Bearer abcdef123455";
    List<String> roles = new ArrayList<>();
    roles.add("ROLE_SELLER");

    UserVo userVo = new UserVo(1L, "hyomyang@naver.com", roles);
    User user = signUpForm().toUser();
    UserDetails userDetails = user;
    when(tokenProvider.getUserVo(anyString())).thenReturn(userVo);
    when(authorGetUserInfoService.getUserInfo(anyLong(), anyString())).thenReturn(
        Optional.ofNullable(user));

    mockMvc.perform(get("/auth/getInfo")
            .header("Authorization", token)
        ).andExpect(status().isOk())
        .andExpect(jsonPath("$.email").value("hyomyang@naver.com"))
        .andExpect(jsonPath("$.roles[0]").value("ROLE_CUSTOMER"));


  }


  private static Sign.Up signUpForm() {
    List<String> list = new ArrayList<>();
    list.add("ROLE_CUSTOMER");
    return Up.builder().email("hyomyang@naver.com").password("string").firstname("string")
        .lastName("string").roles(list).build();
  }


  private static Sign.In signInForm() {
    return new In("hyomyang@naver.com", "string");
  }


}
