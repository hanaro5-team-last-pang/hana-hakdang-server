package com.hanahakdangserver.review.mapper;

import java.time.format.DateTimeFormatter;

import com.hanahakdangserver.review.dto.ReviewResponse;
import com.hanahakdangserver.review.entity.Review;

public class ReviewMapper {

  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(
      "yyyy년 MM월 dd일");

  /**
   * 리뷰 엔티티를 DTO로 변환
   */
  public static ReviewResponse toDto(Review review) {
    return ReviewResponse.builder()
        .id(review.getId())
        .lectureId(review.getLecture().getId())
        .userId(review.getUser().getId())
        .userName(review.getUser().getName())
        .lectureTitle(review.getLecture().getTitle())
        .content(review.getContent())
        .score(convertScoreToFiveScale(review.getScore()))
        .createdAt(review.getCreatedAt().format(DATE_FORMATTER))
        .updatedAt(review.getUpdatedAt().format(DATE_FORMATTER))
        .build();
  }

  /**
   * 10점 만점의 점수를 5점 만점으로 변환
   */
  private static double convertScoreToFiveScale(Integer score) {
    if (score == null) {
      return 0.0;
    }
    return score / 2.0; // 소수점 계산을 위해 2.0으로 나눔
  }
}
