package com.hanahakdangserver.user.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.hanahakdangserver.user.entity.CareerInfo;
import com.hanahakdangserver.user.entity.User;
import com.hanahakdangserver.user.enums.Role;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CareerInfoRepositoryTest {

  private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
  @Autowired
  private CareerInfoRepository careerInfoRepository;
  @Autowired
  private UserRepository userRepository;
  private User user;

  @BeforeEach
  void setUp() {
    user = User.builder()
        .role(Role.MENTEE)
        .name("홍길동")
        .email("honggildong2@example.com")
        .password(passwordEncoder.encode("password"))
        .birthDate(LocalDate.of(1990, 5, 15))
        .profileImageUrl("http://example.com/profile.jpg")
        .build();
    userRepository.save(user);

    CareerInfo careerInfo = CareerInfo.builder()
        .mentor(user) // User 객체는 예시로 가정
        .startDate(LocalDate.of(2000, 1, 1))
        .endDate(LocalDate.of(2023, 12, 31))
        .companyName("디지하나")
        .department("개발")
        .task("Software Development")
        .certifiacteUrl("https://example.com/certificate")
        .build();

    careerInfoRepository.save(careerInfo);
  }

  @DisplayName("findByMentorId 메서드 테스트")
  @Test
  void testFindByMentorId() {
    Long mentorId = user.getId();

    // when
    Optional<CareerInfo> careerInfo = careerInfoRepository.findByMentorId(mentorId);

    // then
    assertThat(careerInfo).isPresent();
    assertThat(careerInfo.get().getMentor().getId()).isEqualTo(mentorId);
    assertThat(careerInfo.get().getTask()).isEqualTo("Software Development");
  }
}