package com.hanahakdangserver.chat.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.hanahakdangserver.common.ResponseDTO;

@Getter
@AllArgsConstructor
public enum ChatResponseSuccessEnum {
  GET_CHAT_MESSAGES_SUCCESS(HttpStatus.OK, "채팅 내역 호출에 성공했습니다.");

  private final HttpStatus httpStatus;
  private final String message;
  
  public <T> ResponseEntity<ResponseDTO<T>> createResponseEntity(T data) {
    ResponseDTO<T> response = ResponseDTO.<T>builder()
        .message(message)
        .result(data)
        .build();
    return ResponseEntity.status(httpStatus).body(response);
  }
}
