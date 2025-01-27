package com.hanahakdangserver.product.enums;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.hanahakdangserver.common.ResponseDTO;
import com.hanahakdangserver.product.dto.HanaItemResponse;

@Getter
@AllArgsConstructor
public enum HanaItemResponseSuccessEnum {
  RECOMMEND_PRODUCTS_SUCCESS(HttpStatus.OK, "추천 상품 리스트 조회에 성공했습니다.");

  private final HttpStatus httpStatus;
  private final String message;

  public ResponseEntity<List<HanaItemResponse>> createResponseEntity(List<HanaItemResponse> data) {
    return ResponseEntity.status(this.httpStatus).body(data);
  }
}