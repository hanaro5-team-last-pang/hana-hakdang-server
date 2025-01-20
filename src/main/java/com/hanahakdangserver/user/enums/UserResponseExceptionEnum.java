package com.hanahakdangserver.user.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Getter
@AllArgsConstructor
public enum UserResponseExceptionEnum {

  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 유저가 존재하지 않습니다.");

  private final HttpStatus httpStatus;
  private final String message;

  public ResponseStatusException createResponseStatusException() {
    return new ResponseStatusException(this.httpStatus, this.message);
  }
}
