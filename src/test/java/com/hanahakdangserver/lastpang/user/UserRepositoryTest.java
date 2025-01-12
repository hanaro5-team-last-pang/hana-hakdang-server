package com.hanahakdangserver.lastpang.user;

import java.time.LocalDate;

import com.hanahakdangserver.user.Role;
import com.hanahakdangserver.user.User;
import com.hanahakdangserver.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class UserRepositoryTest {
  @Autowired
  private UserRepository userRepository;

  @Test
  public void testUserSave() {

    User user = User.builder()
        .role(Role.MENTOR)
        .name("홍길동")
        .email("honggildong@example.com")
        .password("hashedpassword123")
        .birthDt(LocalDate.of(1990, 5, 15))
        .profileImageUrl("http://example.com/profile.jpg")
        .build();

    User savedUser = userRepository.save(user);

    assertThat(savedUser).isNotNull();
    assertThat(savedUser.getId()).isNotNull();
    assertThat(savedUser.getName()).isEqualTo("홍길동");
  }
}
