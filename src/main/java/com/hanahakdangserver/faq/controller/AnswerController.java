package com.hanahakdangserver.faq.controller;

import com.hanahakdangserver.faq.dto.AnswerRequest;
import com.hanahakdangserver.faq.dto.AnswerResponse;
import com.hanahakdangserver.faq.service.AnswerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/faq/{faqId}/answers")
public class AnswerController {

  private static final Logger logger = LoggerFactory.getLogger(AnswerController.class);

  private final AnswerService answerService;

  /**
   * 답변 등록
   *
   * @param faqId
   * @param request
   * @return 등록된 답변
   */
  @PostMapping
  public ResponseEntity<AnswerResponse> createAnswer(
      @PathVariable Long faqId,
      @Valid @RequestBody AnswerRequest request) {

    request = AnswerRequest.builder()
        .userId(request.getUserId())
        .faqId(faqId)
        .content(request.getContent())
        .build();

    AnswerResponse response = answerService.createAnswer(request);

    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }


  /**
   * 답변 삭제
   *
   * @param faqId
   * @param answerId
   * @return null
   */

  @DeleteMapping("/{answerId}")
  public ResponseEntity<Void> deleteAnswer(
      @PathVariable Long faqId,
      @PathVariable Long answerId) {
    logger.info("답변 삭제 요청: faqId={}, answerId={}", faqId, answerId);

    answerService.deleteAnswer(answerId);
    logger.info("답변 삭제 성공: answerId={}", answerId);

    return ResponseEntity.noContent().build();
  }
}
