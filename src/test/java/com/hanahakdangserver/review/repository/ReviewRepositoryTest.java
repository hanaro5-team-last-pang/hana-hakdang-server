package com.hanahakdangserver.review.repository;

import com.hanahakdangserver.classroom.entity.Classroom;
import com.hanahakdangserver.classroom.repository.ClassroomRepository;
import com.hanahakdangserver.lecture.entity.Category;
import com.hanahakdangserver.lecture.entity.Lecture;
import com.hanahakdangserver.lecture.repository.CategoryRepository;
import com.hanahakdangserver.lecture.repository.LectureRepository;
import com.hanahakdangserver.review.entity.Review;
import com.hanahakdangserver.user.entity.CareerInfo;
import com.hanahakdangserver.user.entity.User;
import com.hanahakdangserver.user.enums.Role;
import com.hanahakdangserver.user.repository.CareerInfoRepository;
import com.hanahakdangserver.user.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ReviewRepositoryTest {

  @Autowired
  private ReviewRepository reviewRepository;

  @Autowired
  private LectureRepository lectureRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private CategoryRepository categoryRepository;

  @Autowired
  private CareerInfoRepository careerInfoRepository;

  @Autowired
  private ClassroomRepository classroomRepository;

  private Lecture lecture;
  private User mentor;
  private User reviewer;

  @BeforeEach
  void setUp() {
    CareerInfo careerInfo = CareerInfo.builder()
        .startDate(LocalDate.of(2010, 1, 1))
        .branchName("Seoul")
        .position("Mentor Position")
        .code("MNT001")
        .build();
    careerInfoRepository.save(careerInfo);

    Category category = Category.builder()
        .name("Programming")
        .build();
    categoryRepository.save(category);

    mentor = User.builder()
        .name("Mentor User")
        .email("mentor@example.com")
        .password("password")
        .birthDate(LocalDate.of(1985, 5, 15))
        .role(Role.MENTOR)
        .careerInfo(careerInfo) // CareerInfo 설정
        .build();
    userRepository.save(mentor);

    Classroom classroom = Classroom.builder()
        .id(1L)
        .mentor(mentor)
        .build();
    classroomRepository.save(classroom);

    lecture = Lecture.builder()
        .title("금융 강의")
        .mentor(mentor)
        .classroom(classroom) // Classroom 설정
        .category(category)
        .startTime(LocalDateTime.now())
        .duration(120)
        .maxParticipants(20)
        .tagList(List.of(1, 2, 3))
        .build();
    lectureRepository.save(lecture);

    CareerInfo reviewerCareerInfo = CareerInfo.builder()
        .startDate(LocalDate.of(2015, 3, 1))
        .branchName("Busan")
        .position("Student")
        .code("STD001")
        .build();
    careerInfoRepository.save(reviewerCareerInfo);

    reviewer = User.builder()
        .name("Review User")
        .email("reviewer@example.com")
        .password("password")
        .birthDate(LocalDate.of(1990, 8, 20))
        .role(Role.MENTEE)
        .careerInfo(reviewerCareerInfo) // CareerInfo 설정
        .build();
    userRepository.save(reviewer);

    Review review = Review.builder()
        .lecture(lecture)
        .user(reviewer)
        .content("훌륭한 강의었습니다.")
        .score(5)
        .build();
    reviewRepository.save(review);
  }


  @DisplayName("강의ID 기반 리뷰 조회")
  @Test
  void testFindByLectureId() {
    // When
    List<Review> reviews = reviewRepository.findByLectureId(lecture.getId());

    // Then
    assertThat(reviews).isNotNull();
    assertThat(reviews.size()).isEqualTo(1);
    assertThat(reviews.get(0).getContent()).isEqualTo("훌륭한 강의었습니다.");
    assertThat(reviews.get(0).getUser().getName()).isEqualTo("Review User");
  }

  @DisplayName("멘토에 대한 전체 리뷰")
  @Test
  void testFindReviewsByMentorId() {
    // When
    List<Review> reviews = reviewRepository.findReviewsByMentorId(mentor.getId());

    // Then
    assertThat(reviews).isNotNull();
    assertThat(reviews.size()).isEqualTo(1);
    assertThat(reviews.get(0).getContent()).isEqualTo("훌륭한 강의었습니다.");
    assertThat(reviews.get(0).getLecture().getMentor().getName()).isEqualTo("Mentor User");
  }
}
