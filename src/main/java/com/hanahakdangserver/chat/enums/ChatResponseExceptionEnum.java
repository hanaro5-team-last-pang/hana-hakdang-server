package com.hanahakdangserver.chat.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ChatResponseExceptionEnum {

  FAILED_TO_RETRIEVE_CHAT_MESSAGES("채팅 메시지를 가져오는 데 실패했습니다."),
  INVALID_CLASSROOM_ID("유효하지 않은 강의실 ID입니다."),
  CANT_PROCESS_CHAT_REQUEST("채팅 요청을 처리할 수 없습니다."),
  CANT_DESERIALIZE_CHAT_MESSAGE("채팅 메시지를 역직렬화할 수 없습니다.");

  private final String message;

  /**
   * 예외 메시지로 RuntimeException 생성
   *
   * @return RuntimeException
   */
  public RuntimeException createRuntimeException() {
    return new RuntimeException(message);
  }

  /**
   * 예외 메시지로 RuntimeException 생성 (기존 예외 포함)
   *
   * @param cause 기존 예외
   * @return RuntimeException
   */
  public RuntimeException createRuntimeException(Throwable cause) {
    return new RuntimeException(message, cause);
  }
}
