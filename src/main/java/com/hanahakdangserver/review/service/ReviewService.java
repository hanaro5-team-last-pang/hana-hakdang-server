package com.hanahakdangserver.review.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hanahakdangserver.lecture.entity.Lecture;
import com.hanahakdangserver.lecture.repository.LectureRepository;
import com.hanahakdangserver.review.dto.ReviewRequest;
import com.hanahakdangserver.review.dto.ReviewResponse;
import com.hanahakdangserver.review.entity.Review;
import com.hanahakdangserver.review.mapper.ReviewMapper;
import com.hanahakdangserver.review.projection.ReviewProjection;
import com.hanahakdangserver.review.repository.ReviewRepository;
import com.hanahakdangserver.user.entity.User;
import com.hanahakdangserver.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.hanahakdangserver.review.enums.ReviewResponseExceptionEnum.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

  private final ReviewRepository reviewRepository;
  private final LectureRepository lectureRepository;
  private final UserRepository userRepository;

  private static final int PAGE_SIZE = 3; // 페이지 크기 상수 정의

  /**
   * 강의ID로 리뷰 가져오는 메서드
   *
   * @param lectureId
   * @param page
   * @return 리뷰 데이터 반환
   */
  public ReviewResponse getReviewsByLectureId(Long lectureId, int page) {
    Pageable pageable = PageRequest.of(page, PAGE_SIZE);
    Page<ReviewProjection> reviewProjections = reviewRepository.findReviewsByLectureId(lectureId,
        pageable);

    // 평균 별점 계산
    double averageScore = reviewProjections.stream()
        .mapToDouble(ReviewProjection::getAverageScore)
        .average()
        .orElse(0.0);

    // 별점별 개수 계산
    Map<Integer, Long> scoreCounts = reviewProjections.stream()
        .collect(Collectors.groupingBy(ReviewProjection::getScore, Collectors.counting()));

    List<ReviewResponse.SubScore> subScores = new ArrayList<>();
    for (int i = 5; i >= 1; i--) {
      subScores.add(new ReviewResponse.SubScore(i, scoreCounts.getOrDefault(i, 0L).intValue()));
    }

    // 리뷰 리스트 매핑
    List<ReviewResponse.DetailedReview> detailedReviews = reviewProjections.stream()
        .map(ReviewMapper::toDetailedReview)
        .toList();

    // DTO 생성
    return ReviewResponse.builder()
        .averageScore(String.format("%.1f", averageScore))
        .count((int) reviewProjections.getTotalElements())
        .subScores(subScores)
        .reviews(detailedReviews)
        .build();
  }


  /**
   * 리뷰 생성 메서드
   *
   * @param lectureId
   * @param request
   * @param userId
   * @return 리뷰 데이터
   */
  @Transactional
  public ReviewResponse createReview(Long lectureId, ReviewRequest request, Long userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(USER_NOT_FOUND::createResponseStatusException);

    Lecture lecture = lectureRepository.findById(lectureId)
        .orElseThrow(LECTURE_NOT_FOUND::createResponseStatusException);

    Review review = Review.builder()
        .user(user)
        .lecture(lecture)
        .content(request.getContent())
        .score(request.getScore())
        .build();

    // 리뷰 저장
    reviewRepository.save(review);

    return getReviewsByLectureId(lectureId, 0); // 첫 페이지 리뷰 데이터 반환
  }

  /**
   * 리뷰 삭제
   *
   * @param lectureId
   * @param reviewId
   * @param userId
   */
  @Transactional
  public void deleteReview(Long lectureId, Long reviewId, Long userId) {
    Review review = reviewRepository.findById(reviewId)
        .orElseThrow(REVIEW_NOT_FOUND::createResponseStatusException);

    if (!review.getLecture().getId().equals(lectureId)) {
      throw LECTURE_REVIEW_MISMATCH.createResponseStatusException();
    }

    if (!review.getUser().getId().equals(userId)) {
      throw UNAUTHORIZED_ACTION.createResponseStatusException();
    }

    reviewRepository.delete(review);
  }
}
