package com.hanahakdangserver.faq.repository;

import com.hanahakdangserver.classroom.entity.Classroom;
import com.hanahakdangserver.classroom.repository.ClassroomRepository;
import com.hanahakdangserver.faq.entity.Faq;
import com.hanahakdangserver.lecture.entity.Category;
import com.hanahakdangserver.lecture.entity.Lecture;
import com.hanahakdangserver.lecture.repository.CategoryRepository;
import com.hanahakdangserver.lecture.repository.LectureRepository;
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
public class FaqRepositoryTest {

  @Autowired
  private FaqRepository faqRepository;

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
  private User user;

  @BeforeEach
  void setUp() {
    // CareerInfo 생성 및 저장
    CareerInfo careerInfo = CareerInfo.builder()
        .startDate(LocalDate.of(2015, 1, 1))
        .branchName("Seoul Branch")
        .position("Mentor")
        .code("MNT001")
        .build();
    careerInfoRepository.save(careerInfo);

    // 카테고리 생성 및 저장
    Category category = Category.builder()
        .name("Programming")
        .build();
    categoryRepository.save(category);

    // 유저 생성 및 저장
    user = User.builder()
        .name("Test User")
        .email("test@example.com")
        .password("password")
        .birthDate(LocalDate.of(1990, 5, 20))
        .role(Role.MENTEE)
        .careerInfo(careerInfo)
        .build();
    userRepository.save(user);

    // 강의실 생성 및 저장
    Classroom classroom = Classroom.builder()
        .id(1L)
        .mentor(user)
        .build();
    classroomRepository.save(classroom);

    // 강의 생성 및 저장
    lecture = Lecture.builder()
        .title("Test Lecture")
        .mentor(user)
        .classroom(classroom)
        .category(category)
        .startTime(LocalDateTime.now())
        .duration(120)
        .maxParticipants(30)
        .tagList(List.of(1, 2, 3))
        .build();
    lectureRepository.save(lecture);

    // FAQ 생성 및 저장
    Faq faq = Faq.builder()
        .lecture(lecture)
        .user(user)
        .content("강의는 무엇을 배우나요???")
        .build();
    faqRepository.save(faq);
  }

  @DisplayName("강의 ID를 기준으로 문의 내용 조회")
  @Test
  public void testFindByLectureId() {
    // When
    List<Faq> faqs = faqRepository.findByLectureId(lecture.getId());

    // Then
    assertThat(faqs).isNotNull();
    assertThat(faqs).isNotEmpty();
    assertThat(faqs.get(0).getContent()).isEqualTo("강의는 무엇을 배우나요???");
    assertThat(faqs.get(0).getLecture().getTitle()).isEqualTo("Test Lecture");
    assertThat(faqs.get(0).getUser().getName()).isEqualTo("Test User");
  }
}
