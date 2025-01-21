package com.hanahakdangserver.review.service;

import java.util.List;
import java.util.stream.Collectors;

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

import static com.hanahakdangserver.review.enums.ReviewResponseExceptionEnum.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

  private final ReviewRepository reviewRepository;
  private final LectureRepository lectureRepository;
  private final UserRepository userRepository;

  public List<ReviewResponse> getReviewsByLectureId(Long lectureId) {
    return reviewRepository.findByLectureId(lectureId).stream()
        .map(ReviewMapper::toDto)
        .collect(Collectors.toList());
  }

  @Transactional
  public ReviewResponse createReview(Long lectureId, ReviewRequest request) {
    User user = userRepository.findById(request.getUserId())
        .orElseThrow(USER_NOT_FOUND::createResponseStatusException);

    Lecture lecture = lectureRepository.findById(lectureId)
        .orElseThrow(LECTURE_NOT_FOUND::createResponseStatusException);

    Review review = Review.builder()
        .user(user)
        .lecture(lecture)
        .content(request.getContent())
        .score(request.getScore())
        .build();

    return ReviewMapper.toDto(reviewRepository.save(review));
  }

  @Transactional
  public void deleteReview(Long lectureId, Long reviewId) {
    Review review = reviewRepository.findById(reviewId)
        .orElseThrow(REVIEW_NOT_FOUND::createResponseStatusException);

    if (!review.getLecture().getId().equals(lectureId)) {
      throw LECTURE_REVIEW_MISMATCH.createResponseStatusException();
    }

    reviewRepository.delete(review);
  }
}
