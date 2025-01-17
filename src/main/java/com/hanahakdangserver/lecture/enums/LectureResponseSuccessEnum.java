package com.hanahakdangserver.lecture.enums;

import com.hanahakdangserver.auth.dto.ResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@AllArgsConstructor
public enum LectureResponseSuccessEnum {
  CREATE_LECTURE_SUCCESS(HttpStatus.CREATED, "강의 등록에 성공했습니다."),
  GET_TOTAL_LIST_SUCCESS(HttpStatus.OK, "전체 강의 목록 조회에 성공했습니다.");

  private final HttpStatus httpStatus;
  private final String message;

  public ResponseEntity<ResponseDTO<Object>> createResponseEntity() {
    ResponseDTO<Object> response = ResponseDTO.builder().message(message).build();

    return ResponseEntity.status(httpStatus).body(response);
  }

  public ResponseEntity<ResponseDTO<Object>> createResponseEntity(Object data) {
    ResponseDTO<Object> response = ResponseDTO.builder().message(message).result(data).build();
    return ResponseEntity.status(httpStatus).body(response);
  }
}
