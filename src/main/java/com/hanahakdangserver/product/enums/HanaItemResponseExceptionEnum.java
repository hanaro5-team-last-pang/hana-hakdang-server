package com.hanahakdangserver.product.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Getter
@AllArgsConstructor
public enum HanaItemResponseExceptionEnum {

  LECTURE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 강의를 찾을 수 없습니다."),
  TAGS_NOT_FOUND(HttpStatus.NOT_FOUND, "강의에 연결된 태그가 존재하지 않습니다."),
  PRODUCTS_NOT_FOUND(HttpStatus.NOT_FOUND, "태그에 연결된 상품을 찾을 수 없습니다.");

  private final HttpStatus httpStatus;
  private final String message;

  public ResponseStatusException createResponseStatusException() {
    return new ResponseStatusException(this.httpStatus, this.message);
  }
}
