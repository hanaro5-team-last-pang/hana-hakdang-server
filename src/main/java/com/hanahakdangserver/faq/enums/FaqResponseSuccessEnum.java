package com.hanahakdangserver.faq.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.hanahakdangserver.common.ResponseDTO;

@Getter
@AllArgsConstructor
public enum FaqResponseSuccessEnum {
  CREATE_FAQ_SUCCESS(HttpStatus.CREATED, "문의 등록에 성공했습니다."),
  DELETE_FAQ_SUCCESS(HttpStatus.NO_CONTENT, "문의 삭제에 성공했습니다."),
  GET_FAQ_LIST_SUCCESS(HttpStatus.OK, "문의 조회에 성공했습니다.");

  private final HttpStatus httpStatus;
  private final String message;

  public <T> ResponseEntity<ResponseDTO<T>> createResponseEntity(T data) {
    ResponseDTO<T> response = ResponseDTO.<T>builder()
        .message(this.message)
        .result(data)
        .build();
    return ResponseEntity.status(this.httpStatus).body(response);
  }

  public ResponseEntity<Void> createEmptyResponse() {
    return ResponseEntity.status(this.httpStatus).build();
  }
}
