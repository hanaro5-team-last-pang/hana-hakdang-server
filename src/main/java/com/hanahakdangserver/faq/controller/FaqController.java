package com.hanahakdangserver.faq.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hanahakdangserver.faq.dto.FaqRequest;
import com.hanahakdangserver.faq.dto.FaqResponse;
import com.hanahakdangserver.faq.service.FaqService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/faq")
public class FaqController {

  private final FaqService faqService;


  /**
   * 문의 등록
   *
   * @param lectureId
   * @param request
   * @return 등록된 문의 내용
   */
  @PostMapping("/{lectureId}")
  public ResponseEntity<FaqResponse> createFaq(
      @PathVariable Long lectureId,
      @RequestBody FaqRequest request) {
    FaqResponse response = faqService.createFaq(lectureId, request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  /**
   * 특정 강의에 대한 문의 조회
   *
   * @param lectureId
   * @return 강의에 대한 문의 내용 전체
   */
  @GetMapping("/{lectureId}")
  public ResponseEntity<List<FaqResponse>> getFaqsByLectureId(@PathVariable Long lectureId) {
    List<FaqResponse> faqs = faqService.getFaqsByLectureId(lectureId);
    return ResponseEntity.ok(faqs);
  }


  /**
   * 문의 삭제
   *
   * @param faqId
   * @return null
   */
  @DeleteMapping("/{faqId}")
  public ResponseEntity<Void> deleteFaq(@PathVariable Long faqId) {
    faqService.deleteFaq(faqId);
    return ResponseEntity.noContent().build();
  }
}