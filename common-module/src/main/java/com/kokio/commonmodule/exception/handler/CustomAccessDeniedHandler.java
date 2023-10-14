//package com.kokio.commonmodule.exception.handler;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.kokio.commonmodule.exception.ExceptionController.ExceptionResponse;
//import java.io.IOException;
//import java.io.PrintWriter;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import org.springframework.http.HttpStatus;
//import org.springframework.security.access.AccessDeniedException;
//import org.springframework.security.web.access.AccessDeniedHandler;
//
//
//public class CustomAccessDeniedHandler implements AccessDeniedHandler {
//
//
//  @Override
//  public void handle(HttpServletRequest request, HttpServletResponse response,
//      AccessDeniedException accessDeniedException) throws IOException, ServletException {
//    ExceptionResponse exceptionResponse = new ExceptionResponse(
//        accessDeniedException.getMessage(),
//        accessDeniedException.getClass().getSimpleName(),
//        HttpStatus.FORBIDDEN.value()
//    );
//    response.setContentType("application/json");
//    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//
//    PrintWriter writer = response.getWriter();
//    writer.print(new ObjectMapper().writeValueAsString(exceptionResponse));
//    writer.flush();
//
//  }
//}
