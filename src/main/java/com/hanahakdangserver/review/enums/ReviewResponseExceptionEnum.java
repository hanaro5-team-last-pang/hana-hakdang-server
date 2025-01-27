package com.hanahakdangserver.review.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Getter
@AllArgsConstructor
public enum ReviewResponseExceptionEnum {
  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
  LECTURE_NOT_FOUND(HttpStatus.NOT_FOUND, "강의를 찾을 수 없습니다."),
  REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "리뷰가 존재하지 않습니다."),
  LECTURE_REVIEW_MISMATCH(HttpStatus.BAD_REQUEST, "강의와 리뷰가 일치하지 않습니다."),
  UNAUTHORIZED_ACTION(HttpStatus.FORBIDDEN, "리뷰에 대한 권한이 없습니다.");


  private final HttpStatus httpStatus;
  private final String message;

  public ResponseStatusException createResponseStatusException() {
    return new ResponseStatusException(this.httpStatus, this.message);
  }
}
