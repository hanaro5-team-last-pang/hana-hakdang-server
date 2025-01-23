package com.hanahakdangserver.faq.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

import com.hanahakdangserver.classroom.entity.Classroom;
import com.hanahakdangserver.classroom.repository.ClassroomRepository;
import com.hanahakdangserver.config.ClockConfig;
import com.hanahakdangserver.config.QueryDslConfig;
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

@DataJpaTest
@Import({QueryDslConfig.class, ClockConfig.class}) // QueryDslConfig, ClockConfig를 가져옴
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
  private User mentor;
  private User mentee;
  ;

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

    // 멘토 생성 및 저장
    mentor = User.builder()
        .name("Mentor User")
        .email("test@example.com")
        .password("password")
        .birthDate(LocalDate.of(1990, 5, 20))
        .role(Role.MENTEE)
        .careerInfo(careerInfo)
        .build();
    userRepository.save(mentor);

    // 멘티 생성 및 저장
    mentee = User.builder()
        .name("Mentee User")
        .email("mentee@example.com")
        .password("password")
        .birthDate(LocalDate.of(1990, 8, 20))
        .role(Role.MENTEE)
        .build();
    userRepository.save(mentee);

    // 강의실 생성 및 저장
    Classroom classroom = Classroom.builder()
        .id(1L)
        .build();
    classroomRepository.save(classroom);

    // 강의 생성 및 저장
    lecture = Lecture.builder()
        .title("Test Lecture")
        .mentor(mentor)
        .classroom(classroom)
        .category(category)
        .startTime(LocalDateTime.now())
        .endTime(LocalDateTime.now().plusHours(1))
        .maxParticipants(30)
        .build();
    lectureRepository.save(lecture);

    // FAQ 생성 및 저장
    Faq faq = Faq.builder()
        .lecture(lecture)
        .user(mentee)
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
    assertThat(faqs.get(0).getUser().getName()).isEqualTo("Mentee User");
  }
}
