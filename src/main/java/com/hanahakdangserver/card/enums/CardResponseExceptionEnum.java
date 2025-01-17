package com.hanahakdangserver.card.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Getter
@AllArgsConstructor
public enum CardResponseExceptionEnum {
  CARD_NOT_FOUND(HttpStatus.BAD_REQUEST, "명함이 존재하지 않습니다.");

  private final HttpStatus httpStatus;
  private final String message;

  public ResponseStatusException createResponseStatusException() {
    return new ResponseStatusException(this.httpStatus, this.message);
  }
}
