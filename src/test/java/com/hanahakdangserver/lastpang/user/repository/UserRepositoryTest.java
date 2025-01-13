package com.hanahakdangserver.lastpang.user.repository;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.hanahakdangserver.user.entity.User;
import com.hanahakdangserver.user.enums.Role;
import com.hanahakdangserver.user.repository.UserRepository;

@DataJpaTest
public class UserRepositoryTest {

  @Autowired
  private UserRepository userRepository;

  private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  @DisplayName("유저 생성 테스트")
  @Test
  public void testUserSave() {
    // when
    User user = User.builder()
        .role(Role.MENTEE)
        .name("홍길동")
        .email("honggildong1@example.com")
        .password(passwordEncoder.encode("password"))
        .birthDate(LocalDate.of(1990, 5, 15))
        .profileImageUrl("http://example.com/profile.jpg")
        .build();

    User savedUser = userRepository.save(user);

    // then
    assertThat(savedUser).isNotNull();
    assertThat(savedUser.getName()).isEqualTo("홍길동");
  }
}