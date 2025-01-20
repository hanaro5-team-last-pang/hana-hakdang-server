package com.hanahakdangserver.search.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.hanahakdangserver.common.ResponseDTO;

@Getter
@AllArgsConstructor
public enum SearchResponseSuccessEnum {
  GET_SEARCH_RESULT_SUCCESS(HttpStatus.OK, "검색 결과 조회에 성공했습니다.");

  private final HttpStatus httpStatus;
  private final String message;

  public <T> ResponseEntity<ResponseDTO<T>> createResponseEntity(T data) {
    ResponseDTO<T> response = ResponseDTO.<T>builder().message(message).result(data).build();
    return ResponseEntity.status(httpStatus).body(response);
  }
}
