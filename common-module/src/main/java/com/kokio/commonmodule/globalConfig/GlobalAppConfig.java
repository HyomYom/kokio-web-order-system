package com.kokio.commonmodule.globalConfig;


import com.kokio.commonmodule.exception.ExceptionController;
import com.kokio.commonmodule.security.TokenProvider;
import com.kokio.commonmodule.utill.PageRequest;
import com.querydsl.jpa.impl.JPAQueryFactory;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class GlobalAppConfig implements WebMvcConfigurer {

  @PersistenceContext
  private EntityManager em;

  @Bean
  public ExceptionController exceptionController() {
    return new ExceptionController();
  }

  @Bean
  public JPAQueryFactory queryFactory() {
    return new JPAQueryFactory(em);
  }

  @Bean
  public PageRequest pageRequest() {
    return new PageRequest();
  }

  @Bean
  public TokenProvider tokenProvider() {
    return new TokenProvider();
  }


}
