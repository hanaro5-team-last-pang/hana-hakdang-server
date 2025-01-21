
package com.hanahakdangserver.faq.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hanahakdangserver.faq.dto.FaqRequest;
import com.hanahakdangserver.faq.dto.FaqResponse;
import com.hanahakdangserver.faq.service.FaqService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "FAQ", description = "FAQ 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/faq")
public class FaqController {

  private final FaqService faqService;

  @Operation(summary = "문의 등록", description = "특정 강의에 대한 문의를 등록합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "문의 등록 성공"),
      @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
      @ApiResponse(responseCode = "404", description = "해당 강의가 존재하지 않습니다.")
  })
  @PostMapping("/{lectureId}")
  public ResponseEntity<FaqResponse> createFaq(
      @PathVariable Long lectureId,
      @RequestBody FaqRequest request) {
    FaqResponse response = faqService.createFaq(lectureId, request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @Operation(summary = "문의 조회", description = "특정 강의에 대한 모든 문의를 조회합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "문의 조회 성공"),
      @ApiResponse(responseCode = "404", description = "해당 강의가 존재하지 않습니다.")
  })
  @GetMapping("/{lectureId}")
  public ResponseEntity<List<FaqResponse>> getFaqsByLectureId(@PathVariable Long lectureId) {
    List<FaqResponse> faqs = faqService.getFaqsByLectureId(lectureId);
    return ResponseEntity.ok(faqs);
  }

  @Operation(summary = "문의 삭제", description = "특정 문의를 삭제합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "문의 삭제 성공"),
      @ApiResponse(responseCode = "404", description = "해당 문의가 존재하지 않습니다.")
  })
  @DeleteMapping("/{faqId}")
  public ResponseEntity<Void> deleteFaq(@PathVariable Long faqId) {
    faqService.deleteFaq(faqId);
    return ResponseEntity.noContent().build();
  }
}
