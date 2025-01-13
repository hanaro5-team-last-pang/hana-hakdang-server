package com.hanahakdangserver.lastpang.card.repository;

import java.time.LocalDate;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.hanahakdangserver.user.card.entity.Card;
import com.hanahakdangserver.user.card.repository.CardRepository;
import com.hanahakdangserver.user.entity.User;
import com.hanahakdangserver.user.enums.Role;
import com.hanahakdangserver.user.repository.UserRepository;

@SpringBootTest
public class CardRepositoryTest {

  @Autowired
  private CardRepository cardRepository;
  @Autowired
  private UserRepository userRepository;

  User user;

  @BeforeEach
  void setUp() {
    user = User.builder()
        .role(Role.MENTEE)
        .name("홍길동")
        .email("honggildong@example.com")
        .password("hashedpassword123")
        .birthDate(LocalDate.of(1990, 5, 15))
        .profileImageUrl("http://example.com/profile.jpg")
        .build();

    User savedUser = userRepository.save(user);
  }

  @Test
  public void testCardSave() {
    // 먼저 User 객체를 저장 또는 찾습니다.

    Map<String, String> simpleInfo = Map.of(
        "introduction", "짧은 소개입니다.",
        "hobby", "요리하기"
    );

    Map<String, String> detailInfo = Map.of(
        "experience", "5년 경력의 요리 전문가",
        "specialty", "한식 및 디저트"
    );

    Card card = Card.builder()
        .mentor(user)
        .simpleInfo(simpleInfo)
        .detailInfo(detailInfo)
        .build();

    Card savedCard = cardRepository.save(card);

    assertThat(savedCard).isNotNull();
    assertThat(savedCard.getId()).isNotNull();
  }
}
