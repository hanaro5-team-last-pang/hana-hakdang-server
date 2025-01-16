package com.hanahakdangserver.user.repository;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.hanahakdangserver.config.AuthConfig;
import com.hanahakdangserver.user.entity.User;
import com.hanahakdangserver.user.enums.Role;

@DataJpaTest
@Import(AuthConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class UserRepositoryTest {

  @Autowired
  private PasswordEncoder passwordEncoder;
  @Autowired
  private UserRepository userRepository;

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