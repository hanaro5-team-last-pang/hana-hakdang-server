package com.hanahakdangserver.lecture.enrollment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hanahakdangserver.auth.security.CustomUserDetails;
import com.hanahakdangserver.common.ResponseDTO;
import com.hanahakdangserver.lecture.enrollment.dto.MenteeEnrollmentResponse;
import com.hanahakdangserver.lecture.enrollment.service.MenteeEnrollmentService;
import static com.hanahakdangserver.lecture.enrollment.enums.EnrollmentResponseSuccessEnum.GET_ENROLLMENT_HISTORY_SUCCESS;

@Log4j2
@Tag(name = "멘티 수강신청 조회", description = "멘티 수강신청 조회 API 목록")
@RequiredArgsConstructor
@RestController
@RequestMapping("/lectures")
public class MenteeEnrollmentController {

  private final MenteeEnrollmentService menteeEnrollmentService;

  @Operation(summary = "수강 내역 목록 조회", description = "멘티가 자신이 수강했던 내역 조회를 시도한다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "수강 내역 목록 조회에 성공했습니다."),
  })
  @PreAuthorize("isAuthenticated() and hasRole('MENTEE')")
  @PostMapping("/history/mentee")
  public ResponseEntity<ResponseDTO<MenteeEnrollmentResponse>> getEnrollmentHistoryList(
      @AuthenticationPrincipal
      CustomUserDetails userDetails,
      @RequestParam(value = "page", defaultValue = "0") Integer pageNum) {

    MenteeEnrollmentResponse result = menteeEnrollmentService.getEnrollmentHistoryList(
        userDetails.getId(), pageNum);

    return GET_ENROLLMENT_HISTORY_SUCCESS.createResponseEntity(result);
  }
}
