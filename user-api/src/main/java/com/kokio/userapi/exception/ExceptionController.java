package com.kokio.userapi.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {

  @ExceptionHandler({CustomException.class})
  public ResponseEntity<ExceptionResponse> customRequestException(final CustomException c) {
    return ResponseEntity.status(c.getStatus())
        .body(new ExceptionResponse(c.getMessage(), c.getErrorCode(), c.getStatus()));

  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException e) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied : " + e.getMessage());
  }


  @Getter
  @AllArgsConstructor
  public static class ExceptionResponse {

    private String message;
    private ErrorCode errorCode;
    private int status;
  }
}
