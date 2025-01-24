package com.hanahakdangserver.faq.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.hanahakdangserver.auth.security.CustomUserDetails;
import com.hanahakdangserver.common.ResponseDTO;
import com.hanahakdangserver.faq.dto.FaqRequest;
import com.hanahakdangserver.faq.dto.FaqResponse;
import com.hanahakdangserver.faq.enums.FaqResponseSuccessEnum;
import com.hanahakdangserver.faq.service.FaqService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "FAQ", description = "FAQ 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/lectures/faq")
@Log4j2
public class FaqController {

  private final FaqService faqService;

  @Operation(summary = "FAQ + Answer 조회", description = "FAQ와 답변을 합쳐 페이지네이션으로 조회합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "조회 성공"),
      @ApiResponse(responseCode = "404", description = "해당 강의가 존재하지 않습니다.")
  })
  @GetMapping("/{lectureId}")
  public ResponseEntity<ResponseDTO<List<FaqResponse>>> getPaginatedFaqsByLectureId(
      @PathVariable Long lectureId,
      @RequestParam(defaultValue = "0") int page) {

    List<FaqResponse> paginatedFaqs = faqService.getPaginatedFaqs(lectureId, page);

    log.info("FAQ + 답변 조회 성공: lectureId={}, page={}", lectureId, page);
    return FaqResponseSuccessEnum.GET_FAQ_LIST_SUCCESS.createResponseEntity(paginatedFaqs);
  }

  @PreAuthorize("isAuthenticated() and hasRole('MENTEE')")
  @Operation(summary = "문의 등록", description = "특정 강의에 대한 문의를 등록합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "문의 등록 성공"),
      @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
      @ApiResponse(responseCode = "404", description = "해당 강의가 존재하지 않습니다.")
  })
  @PostMapping("/{lectureId}")
  public ResponseEntity<ResponseDTO<FaqResponse>> createFaq(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @PathVariable Long lectureId,
      @RequestBody FaqRequest request) {

    Long userId = userDetails.getId();
    FaqResponse response = faqService.createFaq(lectureId, request, userId);

    log.info("문의 등록 성공: lectureId={}, userId={}", lectureId, userId);
    return FaqResponseSuccessEnum.CREATE_FAQ_SUCCESS.createResponseEntity(response);
  }


  @PreAuthorize("isAuthenticated()")
  @Operation(summary = "문의 삭제", description = "특정 문의를 삭제합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "문의 삭제 성공"),
      @ApiResponse(responseCode = "403", description = "권한이 없습니다."),
      @ApiResponse(responseCode = "404", description = "해당 문의가 존재하지 않습니다.")
  })
  @DeleteMapping("/{faqId}")
  public ResponseEntity<Void> deleteFaq(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @PathVariable Long faqId) {

    Long userId = userDetails.getId();
    faqService.deleteFaq(faqId, userId);

    log.info("문의 삭제 성공: faqId={}, userId={}", faqId, userId);
    return FaqResponseSuccessEnum.DELETE_FAQ_SUCCESS.createEmptyResponse();
  }
}
