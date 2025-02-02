package com.hanahakdangserver.classroom.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Getter
@RequiredArgsConstructor
public enum ClassroomResponseExceptionEnum {

  CLASSROOM_NOT_USABLE(HttpStatus.BAD_REQUEST, "사용할 수 없는 강의실입니다."),
  NOT_FOUND_CLASSROOM(HttpStatus.NOT_FOUND, "존재하지 않는 강의실입니다."),
  NOT_FOUND_LECTURE(HttpStatus.NOT_FOUND, "강의 정보를 찾을 수 없습니다."),
  LECTURE_CANCELED(HttpStatus.BAD_REQUEST, "강의가 취소됐습니다."),
  NOT_YET_TO_OPEN_CLASSROOM(HttpStatus.BAD_REQUEST, "강의실을 열 수 있는 시간이 아닙니다."),
  NOT_ENROLLED(HttpStatus.BAD_REQUEST, "수강 신청하지 않은 강의입니다.");

  private final HttpStatus httpStatus;
  private final String message;

  public ResponseStatusException createResponseStatusException() {
    return new ResponseStatusException(httpStatus, message);
  }
}
