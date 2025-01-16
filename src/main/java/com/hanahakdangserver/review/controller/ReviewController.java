package com.hanahakdangserver.review.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hanahakdangserver.review.dto.ReviewRequest;
import com.hanahakdangserver.review.dto.ReviewResponse;
import com.hanahakdangserver.review.service.ReviewService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {

  private final ReviewService reviewService;


  /**
   * 특정 강의에 대한 리뷰 조회
   *
   * @param lectureId
   * @return 리뷰 목록
   */
  @GetMapping("/lecture/{lectureId}")
  public ResponseEntity<List<ReviewResponse>> getReviewsByLectureId(@PathVariable Long lectureId) {
    List<ReviewResponse> reviews = reviewService.getReviewsByLectureId(lectureId);
    return ResponseEntity.ok(reviews);
  }


  /**
   * 리뷰 등록
   *
   * @param lectureId
   * @param request
   * @return 등록된 리뷰
   */
  @PostMapping("/lecture/{lectureId}")
  public ResponseEntity<ReviewResponse> createReview(
      @PathVariable Long lectureId,
      @RequestBody ReviewRequest request) {
    ReviewResponse review = reviewService.createReview(lectureId, request);
    return ResponseEntity.status(HttpStatus.CREATED).body(review);
  }


  /**
   * 리뷰 삭제
   *
   * @param lectureId
   * @param reviewId
   * @return null
   */
  @DeleteMapping("/lecture/{lectureId}/{reviewId}")
  public ResponseEntity<Void> deleteReview(
      @PathVariable Long lectureId,
      @PathVariable Long reviewId) {
    reviewService.deleteReview(lectureId, reviewId);
    return ResponseEntity.noContent().build();
  }


  /**
   * 멘토의 전체 강의 리뷰 조회 및 평균 평점 제공 ok
   *
   * @param mentorId
   * @return 평균 평점
   */
  @GetMapping("/mentor/{mentorId}")
  public ResponseEntity<Double> getMentorAverageScore(@PathVariable Long mentorId) {
    Double averageScore = reviewService.getMentorAverageScore(mentorId);
    return ResponseEntity.ok(averageScore);
  }
}
