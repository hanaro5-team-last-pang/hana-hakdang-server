package com.hanahakdangserver.lecture.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.hanahakdangserver.common.ResponseDTO;

@Getter
@AllArgsConstructor
public enum LectureResponseSuccessEnum {
  CREATE_LECTURE_SUCCESS(HttpStatus.CREATED, "강의 등록에 성공했습니다."),
  GET_TOTAL_LIST_SUCCESS(HttpStatus.OK, "전체 강의 목록 조회에 성공했습니다."),
  GET_CATEGORY_LIST_SUCCESS(HttpStatus.OK, "카테고리별 강의 목록 조회에 성공했습니다."),
  GET_LECTURE_DETAIL_SUCCESS(HttpStatus.OK, "특정 강의 상세 조회에 성공했습니다."),
  GET_MENTOR_LECTURES_SUCCESS(HttpStatus.OK, "등록한 강의 목록 조회에 성공했습니다."),
  GET_TOTAL_CATEGORY_LIST_SUCCESS(HttpStatus.OK, "전체 카테고리 목록 조회에 성공했습니다.");

  private final HttpStatus httpStatus;
  private final String message;

  public <T> ResponseEntity<ResponseDTO<T>> createResponseEntity(T data) {
    ResponseDTO<T> response = ResponseDTO.<T>builder().message(message).result(data).build();
    return ResponseEntity.status(httpStatus).body(response);
  }
}
