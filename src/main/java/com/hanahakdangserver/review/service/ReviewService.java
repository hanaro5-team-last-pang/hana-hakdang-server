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
import com.hanahakdangserver.review.repository.ReviewRepository;
import com.hanahakdangserver.user.entity.User;
import com.hanahakdangserver.user.repository.UserRepository;

import java.util.List;

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
   * @return
   */
  public ReviewResponse getReviewsByLectureId(Long lectureId, int page) {
    Pageable pageable = PageRequest.of(page, PAGE_SIZE); // Service에서 Pageable 생성

    // 전체 리뷰 가져오기
    List<Review> allReviews = reviewRepository.findAllByLectureId(lectureId);
    String averageScore = ReviewMapper.calculateAverageScore(allReviews);
    int totalCount = allReviews.size();
    List<ReviewResponse.SubScore> subScores = ReviewMapper.calculateSubScores(allReviews);

    // 페이징 처리된 리뷰 가져오기
    Page<Review> pagedReviews = reviewRepository.findByLectureId(lectureId, pageable);
    List<ReviewResponse.DetailedReview> detailedReviews = pagedReviews.stream()
        .map(ReviewMapper::toDetailedReview)
        .toList();

    // DTO 생성
    return ReviewResponse.builder()
        .averageScore(averageScore)
        .count(totalCount)
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
   * @return
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
