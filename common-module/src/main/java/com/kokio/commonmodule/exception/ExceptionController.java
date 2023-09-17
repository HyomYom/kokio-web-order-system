package com.kokio.commonmodule.exception;

import com.kokio.commonmodule.exception.Code.CartErrorCode;
import com.kokio.commonmodule.exception.Code.ProductErrorCode;
import com.kokio.commonmodule.exception.Code.UserErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ExceptionController {

  @ResponseBody
  @ExceptionHandler({UserException.class})
  public ResponseEntity<ExceptionResponse> userRequestException(final UserException c) {
    return ResponseEntity.status(c.getStatus())
        .body(ExceptionResponse.fromUserErrorCode(c.getUserErrorCode()));
  }

  @ResponseBody
  @ExceptionHandler({ProductException.class})
  public ResponseEntity<ExceptionResponse> productRequestException(final ProductException p) {
    return ResponseEntity.status(p.getStatus())
        .body(ExceptionResponse.fromProductErrorCode(p.getProductErrorCode()));
  }

  @ResponseBody
  @ExceptionHandler({CartException.class})
  public ResponseEntity<ExceptionResponse> cartRequestException(final CartException p) {
    return ResponseEntity.status(p.getStatus())
        .body(ExceptionResponse.fromCartErrorCode(p.getCartErrorCode()));
  }

  @ExceptionHandler(AccessDeniedException.class)
  @ResponseBody
  public ResponseEntity<ExceptionResponse> handleAccessDeniedException(AccessDeniedException e) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
        new ExceptionResponse(e.getMessage(), e.getClass().getSimpleName(),
            HttpStatus.FORBIDDEN.value()));
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
    public static ExceptionResponse fromUserErrorCode(UserErrorCode errorCode) {
      ExceptionResponse response = new ExceptionResponse();
      response.message = errorCode.getDetail();
      response.errorCode = String.valueOf(errorCode);
      response.status = errorCode.getHttpStatus().value();
      return response;
    }

    // ProductErrorCode로 ExceptionResponse 객체 생성
    public static ExceptionResponse fromProductErrorCode(ProductErrorCode errorCode) {
      ExceptionResponse response = new ExceptionResponse();
      response.message = errorCode.getDetail();
      response.errorCode = String.valueOf(errorCode);
      response.status = errorCode.getHttpStatus().value();
      return response;
    }

    public static ExceptionResponse fromCartErrorCode(CartErrorCode errorCode) {
      ExceptionResponse response = new ExceptionResponse();
      response.message = errorCode.getDetail();
      response.errorCode = String.valueOf(errorCode);
      response.status = errorCode.getHttpStatus().value();
      return response;
    }


  }
}
