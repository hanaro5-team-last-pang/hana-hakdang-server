package com.hanahakdangserver.product.controller;

import java.util.List;

import com.hanahakdangserver.product.dto.HanaItemResponse;
import com.hanahakdangserver.product.service.HanaItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class HanaItemController {

  private final HanaItemService hanaItemService;

  /**
   * 태그 리스트를 기반으로 상품 추천
   *
   * @return 추천 상품 리스트
   * @Param tagIds 태그 ID 리스트
   */
  @GetMapping("/recommend/{lectureId}")
  public ResponseEntity<List<HanaItemResponse>> recommendProducts(@PathVariable Long lectureId) {
    List<HanaItemResponse> products = hanaItemService.getItemsByLectureId(lectureId);
    return ResponseEntity.ok(products);
  }

}
