package com.hanahakdangserver.card.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.hanahakdangserver.common.ResponseDTO;

@Getter
@AllArgsConstructor
public enum CardResponseSuccessEnum {
  GET_PROFILE_CARD_SUCCESS(HttpStatus.OK, "명함조회에 성공했습니다");

  private final HttpStatus httpStatus;
  private final String message;

  public ResponseEntity<ResponseDTO<Object>> createResponseEntity() {
    ResponseDTO<Object> response = ResponseDTO.builder().message(message).build();
    return ResponseEntity.status(httpStatus).body(response);
  }

  public <T> ResponseEntity<ResponseDTO<T>> createResponseEntity(T result) {
    ResponseDTO<T> response = ResponseDTO.<T>builder()
        .message(message)
        .result(result)
        .build();

    return ResponseEntity.status(httpStatus).body(response);
  }


}
