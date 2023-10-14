package com.kokio.commonmodule.globalConfig;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class GlobalSecurityConfig extends WebSecurityConfigurerAdapter {


  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .csrf().disable()
        .formLogin().disable()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)// session 방식 로그인은 Unstateless 방식
        .and()
        .authorizeRequests()
        .antMatchers("/swagger-ui.html", "/swagger-ui/**","/v2/api-docs/**", "/v3/api-docs/**", "/auth/register",
            "/auth/register/verify", "/auth/login", "/chat/login")
        .permitAll()
        .anyRequest().authenticated();

  }


  @Override
  public void configure(WebSecurity web) throws Exception {
    web.ignoring()
        .antMatchers("/webjars/**", "/ws/**", "/js/**", "/img/**", "/css/**", "/images/**");

  }


  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }
}
