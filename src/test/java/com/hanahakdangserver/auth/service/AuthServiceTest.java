package com.hanahakdangserver.auth.service;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import com.hanahakdangserver.auth.dto.EmailCheckRequest;
import com.hanahakdangserver.user.repository.UserRepository;


@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

  @InjectMocks
  private AuthService authService;

  @Mock
  private UserRepository userRepository;

  public void testSendEmail() {
    EmailCheckRequest emailDTO = EmailCheckRequest.builder()
        .email("hanahakdang@gmail.com")
        .build();
    authService.sendEmail(emailDTO);
  }

}
