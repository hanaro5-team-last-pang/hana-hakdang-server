package com.hanahakdangserver.review.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.hanahakdangserver.lecture.entity.Lecture;
import com.hanahakdangserver.lecture.repository.LectureRepository;
import com.hanahakdangserver.review.dto.ReviewRequest;
import com.hanahakdangserver.review.dto.ReviewResponse;
import com.hanahakdangserver.review.entity.Review;
import com.hanahakdangserver.review.repository.ReviewRepository;
import com.hanahakdangserver.user.entity.User;
import com.hanahakdangserver.user.enums.Role;
import com.hanahakdangserver.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {

  @Mock
  private ReviewRepository reviewRepository;

  @Mock
  private LectureRepository lectureRepository;

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private ReviewService reviewService;

  private User user;
  private Lecture lecture;
  private Review review;

  @BeforeEach
  void setUp() {
    user = User.builder()
        .id(1L)
        .careerInfo(null)
        .role(Role.MENTEE)
        .name("테스트 사용자")
        .email("test@example.com")
        .password("hashedpassword")
        .birthDate(LocalDate.of(1995, 5, 20))
        .profileImageUrl("http://example/profile.jpg")
        .isActive(true)
        .build();

    lecture = Lecture.builder()
        .id(101L)
        .mentor(user)
        .classroom(null)
        .category(null)
        .title("테스트 강의")
        .startTime(LocalDateTime.of(2024, 2, 1, 10, 0))
        .endTime(LocalDateTime.of(2024, 2, 1, 12, 0))
        .maxParticipants(50)
        .description("테스트 강의")
        .thumbnailUrl("http://example.com/image.jpg")
        .isFull(false)
        .isCanceled(false)
        .isDone(false)
        .build();

    review = Review.builder()
        .id(1001L)
        .user(user)
        .lecture(lecture)
        .content("테스트 리뷰")
        .score(5)
        .build();
  }

  @Test
  @DisplayName("강의 ID로 리뷰 조회 - 정상 동작")
  void getReviewsByLectureId_Success() {
    // Given
    Long lectureId = 101L;
    int page = 0;
    Pageable pageable = PageRequest.of(page, 3);
    when(reviewRepository.findReviewsByLectureId(lectureId, pageable)).thenReturn(
        new PageImpl<>(List.of()));

    // When
    ReviewResponse response = reviewService.getReviewsByLectureId(lectureId, page);

    // Then
    assertNotNull(response);
    assertEquals(0, response.getCount());
  }

  @Test
  @DisplayName("리뷰 생성")
  void createReview_Success() {
    // Given
    Long lectureId = 101L;
    Long userId = 1L;
    ReviewRequest request = new ReviewRequest("좋은 강의입니다.", 5);

    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(lectureRepository.findById(lectureId)).thenReturn(Optional.of(lecture));
    when(reviewRepository.save(any(Review.class))).thenReturn(review);
    when(reviewRepository.findReviewsByLectureId(eq(lectureId), any(Pageable.class)))
        .thenReturn(new PageImpl<>(List.of()));

    ArgumentCaptor<Review> reviewCaptor = ArgumentCaptor.forClass(Review.class); // 전달된 객체 값 확인 가능

    // When
    ReviewResponse response = reviewService.createReview(lectureId, request, userId);

    // Then
    assertNotNull(response);
    verify(reviewRepository).save(reviewCaptor.capture());

    Review capturedReview = reviewCaptor.getValue();
    assertEquals("좋은 강의입니다.", capturedReview.getContent());
    assertEquals(5, capturedReview.getScore());
    assertEquals(user, capturedReview.getUser());
    assertEquals(lecture, capturedReview.getLecture());
  }

  @Test
  @DisplayName("리뷰 삭제 - 성공")
  void deleteReview_Success() {
    // Given
    Long lectureId = 101L;
    Long reviewId = 1001L;
    Long userId = 1L;

    when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));

    ArgumentCaptor<Review> reviewCaptor = ArgumentCaptor.forClass(Review.class);

    // When
    reviewService.deleteReview(lectureId, reviewId, userId);

    // Then
    verify(reviewRepository).delete(reviewCaptor.capture());
    Review capturedReview = reviewCaptor.getValue();
    assertEquals(reviewId, capturedReview.getId());
  }

  @Test
  @DisplayName("리뷰 삭제 - 잘못된 강의 ID 예외 발생")
  void deleteReview_InvalidLecture() {
    // Given
    Long wrongLectureId = 999L;
    Long reviewId = 1001L;
    Long userId = 1L;

    when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));

    // When & Then
    assertThrows(RuntimeException.class,
        () -> reviewService.deleteReview(wrongLectureId, reviewId, userId));
  }

  @Test
  @DisplayName("리뷰 삭제 - 권한 없는 유저 예외 발생")
  void deleteReview_UnauthorizedUser() {
    // Given
    Long lectureId = 101L;
    Long reviewId = 1001L;
    Long anotherUserId = 2L;

    when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));

    // When & Then
    assertThrows(RuntimeException.class,
        () -> reviewService.deleteReview(lectureId, reviewId, anotherUserId));
  }
}


