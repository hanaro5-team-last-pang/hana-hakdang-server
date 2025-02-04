package com.hanahakdangserver.user.service;

import java.io.IOException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.hanahakdangserver.auth.security.CustomUserDetails;
import com.hanahakdangserver.common.S3FileProcessor;
import com.hanahakdangserver.user.dto.AccountRequest;
import com.hanahakdangserver.user.dto.UserInfoResponse;
import com.hanahakdangserver.user.entity.User;
import com.hanahakdangserver.user.enums.Role;
import com.hanahakdangserver.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

  @InjectMocks
  private UserService userService;

  @Mock
  private UserRepository userRepository;

  @Mock
  private S3FileProcessor s3FileProcessor;

  @Spy
  private BCryptPasswordEncoder passwordEncoder;

  private CustomUserDetails customUserDetails;
  private User user;

  /**
   * 공통 설정 : 시큐리티에 유저를 임의 등록 후 진행
   */
  @BeforeEach
  void setup() {
    customUserDetails = CustomUserDetails.builder()
        .id(1L)
        .email("haneul0509@gmail.com")
        .password("1234")
        .isActive(true)
        .authority(new SimpleGrantedAuthority("ROLE_MENTEE"))
        .build();

    UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
        customUserDetails,
        null,
        customUserDetails.getAuthorities()
    );
    SecurityContextHolder.getContext().setAuthentication(authRequest);

    String encodedPassword = passwordEncoder.encode(customUserDetails.getPassword());
    user = User.builder()
        .id(customUserDetails.getId())
        .email(customUserDetails.getUsername())
        .password(encodedPassword)
        .isActive(customUserDetails.isEnabled())
        .role(Role.MENTEE)
        .build();
  }

  @DisplayName("프로필 사진 변경에 대한 계정 수정을 시도한다.")
  @Test
  public void when_UpdateProfile_expect_success() throws IOException {
    // given
    MultipartFile imageFile = new MockMultipartFile(
        "imageFile",
        "test-image.jpg",
        "image/jpeg",
        "content".getBytes()
    );

    String expectedImageUrl = "https://s3.amazonaws.com/bucket/test-image.jpg";
    when(s3FileProcessor.uploadImageFileToS3(any(), eq(imageFile))).thenReturn(
        expectedImageUrl);
    when(userRepository.findById(customUserDetails.getId())).thenReturn(Optional.of(user));

    // when
    userService.updateAccount(customUserDetails.getId(), imageFile, null);

    // then
    verify(s3FileProcessor, times(1)).uploadImageFileToS3(any(), eq(imageFile));
  }

  @DisplayName("비밀번호 변경에 대한 계정 수정을 시도한다.")
  @Test
  public void when_UpdatePassword_expect_success() throws IOException {
    // given
    when(userRepository.findById(customUserDetails.getId())).thenReturn(Optional.of(user));

    AccountRequest accountRequest = AccountRequest.builder()
        .currentPassword("1234")
        .newPassword("5678")
        .confirmPassword("5678")
        .build();

    // when
    userService.updateAccount(customUserDetails.getId(), null, accountRequest);

    // then
    verify(userRepository, times(1)).save(any(User.class));
    verify(passwordEncoder, times(1)).encode("5678");
  }

  @DisplayName("유저조회를 시도한다.")
  @Test
  public void when_GetUserInfo_expect_success() throws IOException {
    // given
    when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

    // when
    UserInfoResponse userInfoResponse = userService.getUserInfo(user.getId());

    // then
    assertNotNull(userInfoResponse);
    verify(userRepository, times(1)).findById(user.getId());
  }
}
