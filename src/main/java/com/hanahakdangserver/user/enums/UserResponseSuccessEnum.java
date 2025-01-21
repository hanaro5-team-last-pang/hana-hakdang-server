package com.hanahakdangserver.user.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.hanahakdangserver.common.ResponseDTO;


@Getter
@AllArgsConstructor
public enum UserResponseSuccessEnum {
  UPDATE_ACCOUNT_SUCCESS(HttpStatus.OK, "계정 수정에 성공했습니다.");

  private final HttpStatus httpStatus;
  private final String message;


  public ResponseEntity<ResponseDTO<Object>> createResponseEntity() {
    ResponseDTO<Object> response = ResponseDTO.builder().message(message).build();
    return ResponseEntity.status(httpStatus).body(response);
  }

}
