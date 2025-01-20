package com.hanahakdangserver.lecture.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Getter
@AllArgsConstructor
public enum LectureResponseExceptionEnum {
  LECTURE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 강의가 존재하지 않습니다."),
  TAG_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 태그가 존재하지 않습니다."),
  CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 카테고리가 존재하지 않습니다.");

  private final HttpStatus httpStatus;
  private final String message;

  public ResponseStatusException createResponseStatusException() {
    return new ResponseStatusException(this.httpStatus, this.message);
  }
}
