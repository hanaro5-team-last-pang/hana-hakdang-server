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
  LECTURE_NOT_FOUND(HttpStatus.NOT_FOUND, "강의를 찾을 수 없습니다."),
  UNAUTHORIZED_ACTION(HttpStatus.FORBIDDEN, "문의 삭제 권한이 없습니다."),
  FAQ_PERMISSION_DENIED(HttpStatus.FORBIDDEN, "해당 강의를 만든 사용자만 답변을 등록할 수 있습니다.");

  private final HttpStatus httpStatus;
  private final String message;

  public ResponseStatusException createResponseStatusException() {
    return new ResponseStatusException(this.httpStatus, this.message);
  }
}
