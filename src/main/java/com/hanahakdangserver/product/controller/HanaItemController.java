
package com.hanahakdangserver.product.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hanahakdangserver.product.dto.HanaItemResponse;
import com.hanahakdangserver.product.service.HanaItemService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "상품", description = "상품 관련 API")
@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class HanaItemController {

  private final HanaItemService hanaItemService;

  @Operation(summary = "상품 추천", description = "강의 ID를 기반으로 상품을 추천합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "추천 상품 리스트 조회 성공"),
      @ApiResponse(responseCode = "404", description = "해당 강의가 존재하지 않습니다.")
  })
  @GetMapping("/recommend/{lectureId}")
  public ResponseEntity<List<HanaItemResponse>> recommendProducts(@PathVariable Long lectureId) {
    List<HanaItemResponse> products = hanaItemService.getItemsByLectureId(lectureId);
    return ResponseEntity.ok(products);
  }
}
