package com.hanahakdangserver.review.mapper;

import com.hanahakdangserver.review.dto.ReviewResponse;
import com.hanahakdangserver.review.entity.Review;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

public class ReviewMapper {

  public static String calculateAverageScore(List<Review> reviews) {
    if (reviews.isEmpty()) {
      return "0.0";
    }
    BigDecimal totalScore = reviews.stream()
        .map(Review::getScore)
        .map(BigDecimal::valueOf)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
    return totalScore.divide(BigDecimal.valueOf(reviews.size()), 1, RoundingMode.HALF_UP)
        .toString();
  }

  public static List<ReviewResponse.SubScore> calculateSubScores(List<Review> reviews) {
    // 별점별 개수 계산
    Map<Integer, Long> scoreCounts = reviews.stream()
        .collect(Collectors.groupingBy(Review::getScore, Collectors.counting()));

    // 5점부터 1점 순으로 정렬된 리스트 생성
    List<ReviewResponse.SubScore> subScores = new ArrayList<>();
    for (int i = 5; i >= 1; i--) {
      subScores.add(new ReviewResponse.SubScore(i, scoreCounts.getOrDefault(i, 0L).intValue()));
    }
    return subScores;
  }

  public static ReviewResponse.DetailedReview toDetailedReview(Review review) {
    return ReviewResponse.DetailedReview.builder()
        .id(review.getId())
        .lectureId(review.getLecture().getId())
        .userId(review.getUser().getId())
        .userName(review.getUser().getName())
        .imageUrl(review.getUser().getProfileImageUrl())
        .lectureTitle(review.getLecture().getTitle())
        .content(review.getContent())
        .score(review.getScore())
        .createdAt(review.getCreatedAt())
        .build();
  }
}
