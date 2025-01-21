//package com.hanahakdangserver.review.controller;
//
//import java.util.List;
//
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.responses.ApiResponses;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import com.hanahakdangserver.common.ResponseDTO;
//import com.hanahakdangserver.review.dto.ReviewRequest;
//import com.hanahakdangserver.review.dto.ReviewResponse;
//import com.hanahakdangserver.review.service.ReviewService;
//import static com.hanahakdangserver.review.enums.ReviewResponseSuccessEnum.*;
//
//
//@Tag(name = "리뷰", description = "리뷰 API 목록")
//@RequiredArgsConstructor
//@RestController
//@RequestMapping("/review")
//public class ReviewController {
//
//  private final ReviewService reviewService;
//
//  @Operation(summary = "특정 강의 리뷰 조회", description = "특정 강의에 등록된 리뷰를 조회합니다.")
//  @ApiResponses({
//      @ApiResponse(responseCode = "200", description = "리뷰 조회 성공"),
//      @ApiResponse(responseCode = "404", description = "해당 강의가 존재하지 않습니다.")
//  })
//  @GetMapping("/lecture/{lectureId}")
//  public ResponseEntity<List<ReviewResponse>> getReviewsByLectureId(@PathVariable Long lectureId) {
//    List<ReviewResponse> reviews = reviewService.getReviewsByLectureId(lectureId);
//    return ResponseEntity.ok(reviews);
//  }
//
//  @Operation(summary = "리뷰 등록", description = "특정 강의에 리뷰를 등록합니다.")
//  @ApiResponses({
//      @ApiResponse(responseCode = "201", description = "리뷰 등록 성공"),
//      @ApiResponse(responseCode = "404", description = "해당 강의 또는 사용자가 존재하지 않습니다.")
//  })
//  @PostMapping("/lecture/{lectureId}")
//  public ResponseEntity<ResponseDTO<ReviewResponse>> createReview(
//      @PathVariable Long lectureId,
//      @RequestBody ReviewRequest request
//  ) {
//    ReviewResponse review = reviewService.createReview(lectureId, request);
//    return CREATE_REVIEW_SUCCESS.createResponseEntity(review);
//  }
//
//
//  @Operation(summary = "리뷰 삭제", description = "특정 강의에 등록된 리뷰를 삭제합니다.")
//  @ApiResponses({
//      @ApiResponse(responseCode = "204", description = "리뷰 삭제 성공"),
//      @ApiResponse(responseCode = "404", description = "해당 리뷰 또는 강의가 존재하지 않습니다.")
//  })
//  @DeleteMapping("/lecture/{lectureId}/{reviewId}")
//  public ResponseEntity<Void> deleteReview(
//      @PathVariable Long lectureId,
//      @PathVariable Long reviewId
//  ) {
//    reviewService.deleteReview(lectureId, reviewId);
//    return ResponseEntity.noContent().build();
//  }
//}


package com.hanahakdangserver.review.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.hanahakdangserver.common.ResponseDTO;
import com.hanahakdangserver.review.dto.ReviewRequest;
import com.hanahakdangserver.review.dto.ReviewResponse;
import com.hanahakdangserver.review.service.ReviewService;
import com.hanahakdangserver.auth.security.CustomUserDetails;

import static com.hanahakdangserver.review.enums.ReviewResponseSuccessEnum.CREATE_REVIEW_SUCCESS;


@Tag(name = "리뷰", description = "리뷰 API 목록")
@RequiredArgsConstructor
@RestController
@RequestMapping("/review")
public class ReviewController {

  private final ReviewService reviewService;

  @Operation(summary = "특정 강의 리뷰 조회", description = "특정 강의에 등록된 리뷰를 조회합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "리뷰 조회 성공"),
      @ApiResponse(responseCode = "404", description = "해당 강의가 존재하지 않습니다.")
  })
  @GetMapping("/lecture/{lectureId}")
  public ResponseEntity<List<ReviewResponse>> getReviewsByLectureId(@PathVariable Long lectureId) {
    List<ReviewResponse> reviews = reviewService.getReviewsByLectureId(lectureId);
    return ResponseEntity.ok(reviews);
  }

  @PreAuthorize("isAuthenticated() and hasRole('MENTEE')")
  @Operation(summary = "리뷰 등록", description = "특정 강의에 리뷰를 등록합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "리뷰 등록 성공"),
      @ApiResponse(responseCode = "404", description = "해당 강의 또는 사용자가 존재하지 않습니다.")
  })
  @PostMapping("/lecture/{lectureId}")
  public ResponseEntity<ResponseDTO<ReviewResponse>> createReview(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @PathVariable Long lectureId,
      @RequestBody ReviewRequest request
  ) {
    Long userId = userDetails.getId();
    ReviewResponse review = reviewService.createReview(lectureId, request, userId);
    return CREATE_REVIEW_SUCCESS.createResponseEntity(review);
  }

  @PreAuthorize("isAuthenticated() and hasRole('MENTEE')")
  @Operation(summary = "리뷰 삭제", description = "특정 강의에 등록된 리뷰를 삭제합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "리뷰 삭제 성공"),
      @ApiResponse(responseCode = "404", description = "해당 리뷰 또는 강의가 존재하지 않습니다.")
  })
  @DeleteMapping("/lecture/{lectureId}/{reviewId}")
  public ResponseEntity<Void> deleteReview(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @PathVariable Long lectureId,
      @PathVariable Long reviewId
  ) {
    Long userId = userDetails.getId();
    reviewService.deleteReview(lectureId, reviewId, userId);
    return ResponseEntity.noContent().build();
  }
}
