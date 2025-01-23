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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hanahakdangserver.auth.security.CustomUserDetails;
import com.hanahakdangserver.common.ResponseDTO;
import com.hanahakdangserver.lecture.enrollment.service.EnrollmentService;
import static com.hanahakdangserver.lecture.enrollment.enums.EnrollmentResponseSuccessEnum.ENROLL_SUCCESS;
import static com.hanahakdangserver.lecture.enrollment.enums.EnrollmentResponseSuccessEnum.WITHDRAW_ENROLLMENT_SUCCESS;

@Log4j2
@Tag(name = "수강신청", description = "수강신청 API 목록")
@RequiredArgsConstructor
@RestController
@RequestMapping("/lectures")
public class EnrollmentController {

  private final EnrollmentService enrollmentService;

  @Operation(summary = "강의 수강신청", description = "멘티가 강의 수강신청을 시도한다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "수강신청에 성공했습니다."),
      @ApiResponse(responseCode = "400", description = "이미 수강신청한 강의입니다."),
      @ApiResponse(responseCode = "400", description = "수강신청 인원이 다 찼습니다."),
      @ApiResponse(responseCode = "403", description = "수강신청이 불가능한 강의입니다.")
  })
  @PreAuthorize("isAuthenticated() and hasRole('MENTEE')")
  @PostMapping("/{lectureId}/enroll")
  public ResponseEntity<ResponseDTO<Void>> enrollLecture(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @PathVariable(value = "lectureId") Long lectureId
  ) {

    enrollmentService.enrollLecture(userDetails.getId(), lectureId);

    return ENROLL_SUCCESS.createResponseEntity(null);
  }

  @Operation(summary = "강의 수강신청 취소", description = "멘티가 강의 수강신청 취소 시도한다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "수강신청 취소에 성공했습니다."),
      @ApiResponse(responseCode = "400", description = "이미 수강신청한 강의입니다."),
      @ApiResponse(responseCode = "400", description = "수강신청 인원이 다 찼습니다."),
      @ApiResponse(responseCode = "403", description = "수강신청이 불가능한 강의입니다.")
  })
  @PreAuthorize("isAuthenticated() and hasRole('MENTEE')")
  @DeleteMapping("/{enrollmentId}/enroll-withdraw")
  public ResponseEntity<ResponseDTO<Void>> withdrawEnrollment(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @PathVariable(value = "enrollmentId") Long enrollmentId
  ) {

    enrollmentService.withdrawEnrollment(userDetails.getId(), enrollmentId);

    return WITHDRAW_ENROLLMENT_SUCCESS.createResponseEntity(null);
  }
}
