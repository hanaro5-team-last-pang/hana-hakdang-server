package com.hanahakdangserver.chat.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.hanahakdangserver.common.ResponseDTO;

@Getter
@RequiredArgsConstructor
public enum ChatResponseSuccessEnum {

  GET_CHAT_MESSAGES_SUCCESS(HttpStatus.OK, "채팅 내역 호출에 성공했습니다.");

  private final HttpStatus httpStatus;
  private final String message;

  /**
   * 성공 응답 생성 메서드
   *
   * @param <T>  데이터 타입
   * @param data 반환할 데이터
   * @return ResponseEntity<ResponseDTO < T>>
   */
  public <T> ResponseEntity<ResponseDTO<T>> createResponseEntity(T data) {
    ResponseDTO<T> response = ResponseDTO.<T>builder()
        .message(message)
        .result(data)
        .build();
    return ResponseEntity.status(httpStatus).body(response);
  }
}
