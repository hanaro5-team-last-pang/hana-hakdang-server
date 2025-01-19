package com.hanahakdangserver.review.service;

import java.util.List;
import java.util.stream.Collectors;

import com.hanahakdangserver.lecture.entity.Lecture;
import com.hanahakdangserver.lecture.repository.LectureRepository;
import com.hanahakdangserver.review.dto.ReviewRequest;
import com.hanahakdangserver.review.dto.ReviewResponse;
import com.hanahakdangserver.review.entity.Review;
import com.hanahakdangserver.review.mapper.ReviewMapper;
import com.hanahakdangserver.review.repository.ReviewRepository;
import com.hanahakdangserver.user.entity.User;
import com.hanahakdangserver.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ReviewService {

  private final ReviewRepository reviewRepository;
  private final LectureRepository lectureRepository;
  private final UserRepository userRepository;

  /**
   * 강의ID 기반 리뷰 조회
   *
   * @param lectureId
   * @return 강의에 대한 리뷰 목록
   */
  public List<ReviewResponse> getReviewsByLectureId(Long lectureId) {
    log.info("Fetching reviews for lectureId={}", lectureId);

    return reviewRepository.findByLectureId(lectureId).stream()
        .map(ReviewMapper::toDto)
        .collect(Collectors.toList());
  }


  /**
   * 리뷰 생성
   *
   * @param lectureId
   * @param request
   * @return 생성된 리뷰
   */

  @Transactional
  public ReviewResponse createReview(Long lectureId, ReviewRequest request) {
    log.info("Creating review for lectureId={}, request={}", lectureId, request);

    User user = userRepository.findById(request.getUserId())
        .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

    Lecture lecture = lectureRepository.findById(lectureId)
        .orElseThrow(() -> new EntityNotFoundException("강의를 찾을 수 없습니다."));

    Review review = Review.builder()
        .user(user)
        .lecture(lecture)
        .content(request.getContent())
        .score(request.getScore())
        .build();

    Review savedReview = reviewRepository.save(review);
    log.info("Review created successfully, reviewId={}", savedReview.getId());

    return ReviewMapper.toDto(savedReview);
  }


  /**
   * 리뷰 삭제
   *
   * @param lectureId
   * @param reviewId
   */
  @Transactional
  public void deleteReview(Long lectureId, Long reviewId) {
    log.info("Deleting review for lectureId={}, reviewId={}", lectureId, reviewId);

    Review review = reviewRepository.findById(reviewId)
        .orElseThrow(() -> new EntityNotFoundException("리뷰가 존재하지 않습니다."));

    if (!review.getLecture().getId().equals(lectureId)) {
      throw new IllegalArgumentException("강의와 리뷰가 일치하지 않습니다.");
    }

    reviewRepository.delete(review);
    log.info("Review deleted successfully, reviewId={}", reviewId);
  }

  /**
   * 멘토의 강의 리뷰 전체 평균
   *
   * @param mentorId
   * @return 강의에 대한 평균 평점
   */
//  public Double getMentorAverageScore(Long mentorId) {
//    log.info("Calculating average score for mentorId={}", mentorId);
//
//    List<Review> mentorReviews = reviewRepository.findReviewsByMentorId(mentorId);
//
//    double averageScore = mentorReviews.stream()
//        .mapToInt(Review::getScore)
//        .average()
//        .orElse(0.0);
//
//    log.info("Average score for mentorId={} is {}", mentorId, averageScore);
//
//    return averageScore;
//  }
}
