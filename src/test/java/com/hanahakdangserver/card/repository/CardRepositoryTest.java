package com.hanahakdangserver.card.repository;

import java.time.LocalDate;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.hanahakdangserver.card.entity.Card;
import com.hanahakdangserver.user.entity.User;
import com.hanahakdangserver.user.enums.Role;
import com.hanahakdangserver.user.repository.UserRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CardRepositoryTest {

  private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
  User user;
  @Autowired
  private CardRepository cardRepository;
  @Autowired
  private UserRepository userRepository;

  //  @BeforeEach
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
  }

  @DisplayName("명함 생성 테스트")
//  @Test
  public void testCardSave() {
    // when
    Map<String, String> simpleInfo = Map.of(
        "introduction", "백엔드 개발자입니다.",
        "hobby", "코딩하기"
    );

    Map<String, String> detailInfo = Map.of(
        "experience", "20년 경력을 소유한 프론트엔드 개발자",
        "skill", "JAVA, SPRING BOOT"
    );

    Card card = Card.builder()
        .mentor(user)
        .shortIntroduction("안녕하세요 홍홍 멘토입니다.")
        .simpleInfo(simpleInfo)
        .detailInfo(detailInfo)
        .build();

    Card savedCard = cardRepository.save(card);

    // then
    assertThat(savedCard).isNotNull();
    assertThat(savedCard.getId()).isNotNull();
  }
}