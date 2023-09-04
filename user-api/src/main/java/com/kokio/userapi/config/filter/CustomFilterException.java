package com.kokio.userapi.config.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kokio.commonmodule.exception.Code.ProductErrorCode;
import com.kokio.commonmodule.exception.Code.UserErrorCode;
import com.kokio.commonmodule.exception.UserException;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


@Component
public class CustomFilterException extends OncePerRequestFilter {


  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      filterChain.doFilter(request, response);
    } catch (UserException e) {
      response.setStatus(e.getStatus());
      response.setContentType(MediaType.APPLICATION_JSON_VALUE);
      response.setCharacterEncoding("UTF-8");
      objectMapper.writeValue(response.getWriter(),
          ExceptionResponse.fromUserErrorCode(e.getUserErrorCode()));
    }

  }

  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @ToString
  public static class ExceptionResponse {

    private String message;
    private String errorCode;
    private int status;

    // UserErrorCode로 ExceptionResponse 객체 생성
    public static ExceptionResponse fromUserErrorCode(
        UserErrorCode errorCode) {
      ExceptionResponse response = new ExceptionResponse();
      response.message = errorCode.getDetail();
      response.errorCode = String.valueOf(errorCode);
      response.status = errorCode.getHttpStatus().value();
      return response;
    }

    // ProductErrorCode로 ExceptionResponse 객체 생성
    public static ExceptionResponse fromProductErrorCode(
        ProductErrorCode errorCode) {
      ExceptionResponse response = new ExceptionResponse();
      response.message = errorCode.getDetail();
      response.errorCode = String.valueOf(errorCode);
      response.status = errorCode.getHttpStatus().value();
      return response;
    }

  }
}
