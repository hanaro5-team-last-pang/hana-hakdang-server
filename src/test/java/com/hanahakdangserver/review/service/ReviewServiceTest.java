package com.hanahakdangserver.review.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.hanahakdangserver.lecture.enrollment.repository.EnrollmentRepository;
import com.hanahakdangserver.lecture.entity.Lecture;
import com.hanahakdangserver.lecture.repository.LectureRepository;
import com.hanahakdangserver.review.dto.ReviewRequest;
import com.hanahakdangserver.review.dto.ReviewResponse;
import com.hanahakdangserver.review.entity.Review;
import com.hanahakdangserver.review.repository.ReviewRepository;
import com.hanahakdangserver.user.entity.User;
import com.hanahakdangserver.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

  @Mock
  private ReviewRepository reviewRepository;
  @Mock
  private LectureRepository lectureRepository;
  @Mock
  private UserRepository userRepository;
  @Mock
  private EnrollmentRepository enrollmentRepository;

  @InjectMocks
  private ReviewService reviewService;

  private User user;
  private Lecture lecture;
  private Review review;

  @BeforeEach
  void setUp() {
    user = User.builder()
        .id(1L)
        .name("테스트 유저")
        .build();

    lecture = Lecture.builder()
        .id(101L)
        .title("테스트 강의")
        .mentor(user)
        .build();

    review = Review.builder()
        .id(1001L)
        .user(user)
        .lecture(lecture)
        .content("좋은 강의입니다.")
        .score(5)
        .build();
  }

  @Test
  @DisplayName("리뷰 생성 - 강의를 수강한 사용자만 가능")
  void createReview_Success() {
    // Given
    Long lectureId = 101L;
    Long userId = 1L;
    ReviewRequest request = new ReviewRequest("좋은 강의입니다.", 5);

    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(lectureRepository.findById(lectureId)).thenReturn(Optional.of(lecture));
    when(enrollmentRepository.existsByUserAndLecture(user, lecture)).thenReturn(true);
    when(reviewRepository.save(any(Review.class))).thenReturn(review);
    when(reviewRepository.findReviewsByLectureId(eq(lectureId), any(Pageable.class)))
        .thenReturn(new PageImpl<>(List.of()));

    // When
    ReviewResponse response = reviewService.createReview(lectureId, request, userId);

    // Then
    assertNotNull(response);
    verify(reviewRepository).save(any(Review.class));
  }

  @Test
  @DisplayName("리뷰 생성 - 강의를 수강하지 않은 사용자는 예외 발생")
  void createReview_Fail() {
    // Given
    Long lectureId = 101L;
    Long userId = 1L;
    ReviewRequest request = new ReviewRequest("좋은 강의입니다.", 5);

    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(lectureRepository.findById(lectureId)).thenReturn(Optional.of(lecture));
    when(enrollmentRepository.existsByUserAndLecture(user, lecture)).thenReturn(false);

    // When & Then
    assertThrows(RuntimeException.class,
        () -> reviewService.createReview(lectureId, request, userId));
  }

  @Test
  @DisplayName("리뷰 삭제 - 강의를 수강한 사용자만 가능")
  void deleteReview_Success() {
    // Given
    Long lectureId = 101L;
    Long reviewId = 1001L;
    Long userId = 1L;

    when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
    when(enrollmentRepository.existsByUserAndLecture(user, lecture)).thenReturn(true);

    // When
    reviewService.deleteReview(lectureId, reviewId, userId);

    // Then
    verify(reviewRepository).delete(any(Review.class));
  }

  @Test
  @DisplayName("리뷰 삭제 - 강의를 수강하지 않은 사용자는 예외 발생")
  void deleteReview_Fail_NotEnroll() {
    // Given
    Long lectureId = 101L;
    Long reviewId = 1001L;
    Long userId = 1L;

    when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
    when(enrollmentRepository.existsByUserAndLecture(user, lecture)).thenReturn(false);

    // When & Then
    assertThrows(RuntimeException.class,
        () -> reviewService.deleteReview(lectureId, reviewId, userId));
  }

  @Test
  @DisplayName("리뷰 삭제 - 본인이 작성한 리뷰만 삭제 가능")
  void deleteReview_Fail_WrongUser() {
    // Given
    Long lectureId = 101L;
    Long reviewId = 1001L;
    Long otherUserId = 2L;

    when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));

    // When & Then
    assertThrows(RuntimeException.class,
        () -> reviewService.deleteReview(lectureId, reviewId, otherUserId));
  }

  @Test
  @DisplayName("리뷰 삭제 - 존재하지 않는 리뷰 삭제 시 예외 발생")
  void deleteReview_Fail_ReviewNotFound() {
    // Given
    Long lectureId = 101L;
    Long reviewId = 999L;
    Long userId = 1L;

    when(reviewRepository.findById(reviewId)).thenReturn(Optional.empty());

    // When & Then
    assertThrows(RuntimeException.class,
        () -> reviewService.deleteReview(lectureId, reviewId, userId));
  }
}
