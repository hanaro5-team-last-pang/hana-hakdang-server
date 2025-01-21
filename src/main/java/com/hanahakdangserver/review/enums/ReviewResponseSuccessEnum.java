package com.hanahakdangserver.review.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.hanahakdangserver.common.ResponseDTO;

@Getter
@AllArgsConstructor
public enum ReviewResponseSuccessEnum {
  CREATE_REVIEW_SUCCESS(HttpStatus.CREATED, "리뷰 등록에 성공했습니다."),
  DELETE_REVIEW_SUCCESS(HttpStatus.NO_CONTENT, "리뷰 삭제에 성공했습니다."),
  GET_REVIEW_LIST_SUCCESS(HttpStatus.OK, "리뷰 조회에 성공했습니다.");

  private final HttpStatus httpStatus;
  private final String message;

  public <T> ResponseEntity<ResponseDTO<T>> createResponseEntity(T data) {
    ResponseDTO<T> response = ResponseDTO.<T>builder()
        .message(this.message)
        .result(data)
        .build();
    return ResponseEntity.status(this.httpStatus).body(response);
  }
}
