package com.hanahakdangserver.lecture.enrollment.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.hanahakdangserver.common.ResponseDTO;

@Getter
@AllArgsConstructor
public enum EnrollmentResponseSuccessEnum {
  ENROLL_SUCCESS(HttpStatus.OK, "수강신청에 성공했습니다."),
  WITHDRAW_ENROLLMENT_SUCCESS(HttpStatus.OK, "수강신청 취소에 성공했습니다."),
  GET_ENROLLMENT_HISTORY_SUCCESS(HttpStatus.OK, "수강 내역 목록 조회에 성공했습니다."),
  GET_ENROLLMENT_QUEUE_SUCCESS(HttpStatus.OK, "수강 예정 내역 목록 조회에 성공했습니다.");

  private final HttpStatus httpStatus;
  private final String message;

  public <T> ResponseEntity<ResponseDTO<T>> createResponseEntity(T data) {
    ResponseDTO<T> response = ResponseDTO.<T>builder().message(message).result(data).build();
    return ResponseEntity.status(httpStatus).body(response);
  }
}
