package com.hanahakdangserver.classroom.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.hanahakdangserver.common.ResponseDTO;

@Getter
@RequiredArgsConstructor
public enum ClassroomResponseSuccessEnum {
  CLASSROOM_STARTED(HttpStatus.OK, "강의를 시작합니다"),
  CLASSROOM_ENTERED(HttpStatus.OK, "강의실에 입장합니다.");

  private final HttpStatus httpStatus;
  private final String message;

  public <T> ResponseEntity<ResponseDTO<T>> createResponseEntity(T body) {
    ResponseDTO<T> response = ResponseDTO.<T>builder()
        .message(message)
        .result(body)
        .build();
    return ResponseEntity.status(httpStatus).body(response);
  }
}
