package com.hanahakdangserver.news.enums;

import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.hanahakdangserver.common.ResponseDTO;
import com.hanahakdangserver.news.dto.NewsResponse;

@Getter
@RequiredArgsConstructor
public enum NewsResponseSuccessEnum {
  NEWS_FETCHED(HttpStatus.OK, "뉴스 조회에 성공했습니다."),
  CRAWLING_REQUESTED(HttpStatus.OK, "뉴스 크롤링 요청이 완료되었습니다.");

  private final HttpStatus httpStatus;
  private final String message;

  public ResponseEntity<ResponseDTO<List<NewsResponse>>> createResponseEntity(
      List<NewsResponse> body) {
    ResponseDTO<List<NewsResponse>> response = ResponseDTO.<List<NewsResponse>>builder()
        .message(message)
        .result(body)
        .build();
    return ResponseEntity.status(httpStatus).body(response);
  }

  public ResponseEntity<ResponseDTO<String>> createResponseEntity(String body) {
    ResponseDTO<String> response = ResponseDTO.<String>builder()
        .message(message)
        .result(body)
        .build();
    return ResponseEntity.status(httpStatus).body(response);
  }

  public ResponseEntity<ResponseDTO<NewsResponse>> createResponseEntity(NewsResponse body) {
    ResponseDTO<NewsResponse> response = ResponseDTO.<NewsResponse>builder()
        .message(message)
        .result(body)
        .build();
    return ResponseEntity.status(httpStatus).body(response);
  }

}
