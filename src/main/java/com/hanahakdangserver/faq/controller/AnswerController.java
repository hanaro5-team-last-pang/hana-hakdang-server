package com.hanahakdangserver.faq.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.hanahakdangserver.faq.dto.AnswerRequest;
import com.hanahakdangserver.faq.dto.AnswerResponse;
import com.hanahakdangserver.faq.service.AnswerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "FAQ 답변", description = "FAQ 답변 관련 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("lectures/faq/{faqId}/answers")
@Log4j2
public class AnswerController {

  private final AnswerService answerService;

  @PreAuthorize("isAuthenticated() and hasRole('MENTOR')")
  @Operation(summary = "답변 등록", description = "FAQ에 답변을 등록합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "답변 등록 성공"),
      @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
      @ApiResponse(responseCode = "404", description = "해당 FAQ가 존재하지 않습니다.")
  })
  @PostMapping
  public ResponseEntity<AnswerResponse> createAnswer(
      @PathVariable Long faqId,
      @Valid @RequestBody AnswerRequest request) {

    AnswerResponse response = answerService.createAnswer(request);

    log.info("답변 등록 성공: faqId={}, answerId={}", faqId, response.getId());

    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @PreAuthorize("isAuthenticated() and hasRole('MENTOR')")
  @Operation(summary = "답변 삭제", description = "FAQ에 등록된 답변을 삭제합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "답변 삭제 성공"),
      @ApiResponse(responseCode = "404", description = "해당 FAQ나 답변이 존재하지 않습니다.")
  })
  @DeleteMapping("/{answerId}")
  public ResponseEntity<Void> deleteAnswer(
      @PathVariable Long faqId,
      @PathVariable Long answerId) {
    log.info("답변 삭제 요청: faqId={}, answerId={}", faqId, answerId);

    answerService.deleteAnswer(answerId);

    log.info("답변 삭제 성공: faqId={}, answerId={}", faqId, answerId);

    return ResponseEntity.noContent().build();
  }
}
