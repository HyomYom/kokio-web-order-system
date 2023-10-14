package com.kokio.userapi.config;

import com.kokio.commonmodule.globalConfig.GlobalSecurityConfig;
import com.kokio.userapi.config.filter.CustomFilterException;
import com.kokio.userapi.config.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class UserSecurityConfig extends GlobalSecurityConfig {

  private final JwtAuthenticationFilter jwtAuthenticationFilter;
  private final CustomFilterException customFilterException;


  @Override
  protected void configure(HttpSecurity http) throws Exception {
    super.configure(http);
    http
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .addFilterBefore(customFilterException, JwtAuthenticationFilter.class)
    ;

  }

  @Override
  public void configure(WebSecurity web) throws Exception {
    super.configure(web);
  }

  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }
}
