package com.hanahakdangserver.lecture.repository;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.apache.zookeeper.Op;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.hanahakdangserver.classroom.entity.Classroom;
import com.hanahakdangserver.classroom.repository.ClassroomRepository;
import com.hanahakdangserver.classroom.utils.SnowFlakeGenerator;
import com.hanahakdangserver.config.ClockConfig;
import com.hanahakdangserver.config.QueryDslConfig;
import com.hanahakdangserver.lecture.entity.Category;
import com.hanahakdangserver.lecture.entity.Lecture;
import com.hanahakdangserver.lecture.projection.MentorIdOnly;
import com.hanahakdangserver.user.entity.User;
import com.hanahakdangserver.user.enums.Role;
import com.hanahakdangserver.user.repository.UserRepository;

@DataJpaTest
@Import({QueryDslConfig.class, ClockConfig.class, SnowFlakeGenerator.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class LectureRepositoryTest {

  @Autowired
  private LectureRepository lectureRepository;

  @Autowired
  private ClassroomRepository classroomRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private SnowFlakeGenerator snowFlakeGenerator;

  @Autowired
  private CategoryRepository categoryRepository;

  private User mentor;

  @BeforeEach
  void setUp() {
    mentor = User.builder()
        .name("Mentor")
        .birthDate(LocalDate.now())
        .password("password")
        .role(Role.MENTOR)
        .email("mentor@hanahakdang.com")
        .isActive(true)
        .build();
    userRepository.save(mentor);
  }

  @Test
  @DisplayName("강의실 아이디로 최신 강의 아이디 찾기")
  void testFindLectureIdByClassroomID() {
    Classroom classroom = Classroom.builder()
        .id(snowFlakeGenerator.nextId())
        .isUsed(true)
        .build();
    classroomRepository.save(classroom);

    Category category = Category.builder().name("카테고리1").build();
    categoryRepository.save(category);

    List<Lecture> lectureList = List.of(Lecture.builder()
            .mentor(mentor)
            .classroom(classroom)
            .category(category)
            .title("강의1")
            .startTime(LocalDateTime.now())
            .duration(123)
            .maxParticipants(2)
            .description("123")
            .build(),
        Lecture.builder()
            .mentor(mentor)
            .classroom(classroom)
            .category(category)
            .title("강의1")
            .startTime(LocalDateTime.now())
            .duration(123)
            .maxParticipants(2)
            .description("123")
            .build()
    );
    lectureRepository.saveAll(lectureList);

    Lecture latestLecture = Lecture.builder()
        .mentor(mentor)
        .classroom(classroom)
        .category(category)
        .title("강의1")
        .startTime(LocalDateTime.now())
        .duration(123)
        .maxParticipants(2)
        .description("123")
        .build();
    lectureRepository.save(latestLecture);

    Optional<Lecture> foundLecture = lectureRepository.findLatestLectureIdByClassroomId(
        classroom.getId());

    assertThat(foundLecture.isPresent()).isTrue();
    assertThat(foundLecture.get()).isEqualTo(latestLecture);
  }

  @Test
  @DisplayName("강의실 멘토 ID 검색 테스트")
  void testFindMentorIdById() {
    Classroom classroom = Classroom.builder()
        .id(snowFlakeGenerator.nextId())
        .isUsed(true)
        .build();
    classroomRepository.save(classroom);

    Category category = Category.builder().name("카테고리1").build();
    categoryRepository.save(category);
    Lecture lecture = Lecture.builder()
        .mentor(mentor)
        .classroom(classroom)
        .category(category)
        .title("강의1")
        .startTime(LocalDateTime.now())
        .duration(123)
        .maxParticipants(2)
        .description("123")
        .build();
    lectureRepository.save(lecture);
    Optional<MentorIdOnly> result = lectureRepository.findMentorIdById(lecture.getId());
    assertThat(result.isPresent()).isTrue();
    assertThat(result.get().getMentorId()).isEqualTo(mentor.getId());
  }
}
