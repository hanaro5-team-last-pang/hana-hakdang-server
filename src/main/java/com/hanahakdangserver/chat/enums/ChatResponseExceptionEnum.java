package com.hanahakdangserver.chat.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
//
//@Getter
//@AllArgsConstructor
//public enum ChatResponseExceptionEnum {
//
//  FAILED_TO_RETRIEVE_CHAT_MESSAGES("채팅 메시지를 가져오는 데 실패했습니다."),
//  INVALID_CLASSROOM_ID("유효하지 않은 강의실 ID입니다."),
//  CANT_PROCESS_CHAT_REQUEST("채팅 요청을 처리할 수 없습니다."),
//  CANT_DESERIALIZE_CHAT_MESSAGE("채팅 메시지를 역직렬화할 수 없습니다.");
//
//  private final String message;
//
//  /**
//   * 예외 메시지로 RuntimeException 생성
//   *
//   * @return RuntimeException
//   */
//  public RuntimeException createRuntimeException() {
//    return new RuntimeException(message);
//  }
//
//  /**
//   * 예외 메시지로 RuntimeException 생성 (기존 예외 포함)
//   *
//   * @param cause 기존 예외
//   * @return RuntimeException
//   */
//  public RuntimeException createRuntimeException(Throwable cause) {
//    return new RuntimeException(message, cause);
//  }
//}

@Getter
@RequiredArgsConstructor
public enum ChatResponseExceptionEnum {

  FAILED_TO_RETRIEVE_CHAT_MESSAGES(HttpStatus.INTERNAL_SERVER_ERROR, "채팅 메시지를 가져오는 데 실패했습니다."),
  INVALID_CLASSROOM_ID(HttpStatus.BAD_REQUEST, "유효하지 않은 강의실 ID입니다."),
  CANT_PROCESS_CHAT_REQUEST(HttpStatus.INTERNAL_SERVER_ERROR, "채팅 요청을 처리할 수 없습니다."),
  CANT_DESERIALIZE_CHAT_MESSAGE(HttpStatus.INTERNAL_SERVER_ERROR, "채팅 메시지를 역직렬화할 수 없습니다.");

  private final HttpStatus httpStatus;
  private final String message;

  /**
   * 예외 메시지로 ResponseStatusException 생성
   *
   * @return ResponseStatusException
   */
  public ResponseStatusException createResponseStatusException() {
    return new ResponseStatusException(httpStatus, message);
  }
  
}
