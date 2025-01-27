package com.hanahakdangserver.news.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Getter
@AllArgsConstructor
public enum NewsResponseExceptionEnum {

  NEWS_FETCH_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "뉴스 데이터를 가져오는 데 실패했습니다."),
  INVALID_DATE_FORMAT(HttpStatus.BAD_REQUEST, "날짜 형식이 올바르지 않습니다."),
  NEWS_SAVE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "뉴스 데이터를 저장하는 데 실패했습니다."),
  NO_NEWS_FOUND(HttpStatus.NOT_FOUND, "저장된 뉴스가 없습니다.");

  private final HttpStatus httpStatus;
  private final String message;

  public ResponseStatusException createResponseStatusException() {
    return new ResponseStatusException(this.httpStatus, this.message);
    
  }
}


