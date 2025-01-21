package com.hanahakdangserver.faq.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Getter
@AllArgsConstructor
public enum FaqResponseExceptionEnum {

  FAQ_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 문의가 존재하지 않습니다."),
  ANSWER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 답변이 존재하지 않습니다."),
  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
  LECTURE_NOT_FOUND(HttpStatus.NOT_FOUND, "강의를 찾을 수 없습니다.");


  private final HttpStatus httpStatus;
  private final String message;

  public ResponseStatusException createResponseStatusException() {
    return new ResponseStatusException(this.httpStatus, this.message);
  }
}
