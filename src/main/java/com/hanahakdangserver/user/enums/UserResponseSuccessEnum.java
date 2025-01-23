package com.hanahakdangserver.user.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.hanahakdangserver.common.ResponseDTO;


@Getter
@AllArgsConstructor
public enum UserResponseSuccessEnum {
  UPDATE_ACCOUNT_SUCCESS(HttpStatus.OK, "계정 수정에 성공했습니다."),
  FETCH_USER_SUCCESS(HttpStatus.OK, "유저 정보를 성공적으로 반환했습니다.");

  private final HttpStatus httpStatus;
  private final String message;

  public <T> ResponseEntity<ResponseDTO<T>> createResponseEntity(T data) {
    return ResponseEntity.status(this.httpStatus)
        .body(new ResponseDTO<>(this.message, data));
  }

}
