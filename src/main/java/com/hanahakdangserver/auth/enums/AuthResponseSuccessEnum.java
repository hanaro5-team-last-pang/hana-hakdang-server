package com.hanahakdangserver.auth.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import com.hanahakdangserver.auth.dto.ResponseDTO;


@Getter
@AllArgsConstructor
public enum AuthResponseSuccessEnum {
  SIGN_UP_SUCCESS(HttpStatus.CREATED, "회원가입에 성공했습니다."),
  LOG_IN_SUCCESS(HttpStatus.OK, "로그인에 성공했습니다."),
  EMAIL_CHECK_SEND_SUCCESS(HttpStatus.OK, "이메일이 전송됐습니다."),
  EMAIL_CONFIRMED(HttpStatus.OK, "이메일 인증이 완료됐습니다.");

  private final HttpStatus httpStatus;
  private final String message;


  public ResponseEntity<ResponseDTO<Object>> createResponseEntity() {
    ResponseDTO<Object> response = ResponseDTO.builder().message(message).build();
    return ResponseEntity.status(httpStatus).body(response);
  }

}
