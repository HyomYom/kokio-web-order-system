package com.kokio.userapi.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@ControllerAdvice
public class ExceptionController {

  @ExceptionHandler({CustomException.class})
  public ResponseEntity<ExceptionResponse> customRequestException(final CustomException c){
    return ResponseEntity.status(c.getStatus()).body(new ExceptionResponse(c.getMessage(), c.getErrorCode(), c.getStatus()));

  }


  @Getter
  @AllArgsConstructor
  public static class ExceptionResponse {
    private String message;
    private ErrorCode errorCode;
    private int status;
  }
}
