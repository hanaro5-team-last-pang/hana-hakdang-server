package com.hanahakdangserver.lecture.enrollment.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Getter
@AllArgsConstructor
public enum EnrollmentResponseExceptionEnum {
  INVALID_ENROLLMENT(HttpStatus.BAD_REQUEST, "수강신청 내역을 찾을 수 없습니다."),
  NOT_ENROLLED(HttpStatus.BAD_REQUEST, "수강신청하지 않은 강의입니다."),
  ENROLL_IS_NOT_ALLOWED(HttpStatus.FORBIDDEN, "수강신청이 불가능한 강의입니다."),
  ALREADY_ENROLLED(HttpStatus.BAD_REQUEST, "이미 수강신청한 강의입니다."),
  LECTURE_IS_FULL(HttpStatus.BAD_REQUEST, "수강신청 인원이 다 찼습니다.");

  private final HttpStatus httpStatus;
  private final String message;

  public ResponseStatusException createResponseStatusException() {
    return new ResponseStatusException(this.httpStatus, this.message);
  }
}
