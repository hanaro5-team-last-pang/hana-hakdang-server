package com.hanahakdangserver.auth.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Getter
@AllArgsConstructor
public enum AuthResponseExceptionEnum {
  EMAIL_NOT_CONFIRMED(HttpStatus.BAD_REQUEST, "이메일 인증이 완료되지 않았습니다."),
  EMAIL_DUPLICATED(HttpStatus.BAD_REQUEST, "해당 이메일은 존재합니다."),
  PASSWORD_NOT_MATCHED(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
  EMAIL_CHECK_EXPIRED(HttpStatus.BAD_REQUEST, "이메일 인증 시간이 만료됐습니다."),
  EMAIL_NOT_FOUND(HttpStatus.BAD_REQUEST, "이메일을 찾을 수 없습니다."),
  TOKEN_NOT_MATCHED(HttpStatus.BAD_REQUEST, "토큰이 일치하지 않습니다.");


  private final HttpStatus httpStatus;
  private final String message;

  public ResponseStatusException createResponseStatusException() {
    return new ResponseStatusException(this.httpStatus, this.message);
  }
}
