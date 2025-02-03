package com.hanahakdangserver.user.service;

import java.io.IOException;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.hanahakdangserver.common.S3FileProcessor;
import com.hanahakdangserver.user.dto.AccountRequest;
import com.hanahakdangserver.user.dto.UserInfoResponse;
import com.hanahakdangserver.user.entity.User;
import com.hanahakdangserver.user.repository.UserRepository;
import static com.hanahakdangserver.auth.enums.AuthResponseExceptionEnum.NEW_PASSWORD_NOT_MATCHED;
import static com.hanahakdangserver.auth.enums.AuthResponseExceptionEnum.PASSWORD_BLANK;
import static com.hanahakdangserver.auth.enums.AuthResponseExceptionEnum.PASSWORD_NOT_MATCHED;
import static com.hanahakdangserver.user.enums.UserResponseExceptionEnum.USER_NOT_FOUND;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)

public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final S3FileProcessor s3FileProcessor;

  @Value("${aws.s3.bucketName}")
  private String bucket;

  /**
   * 계정 수정 기능(비밀번호, 프로필 이미지 변경)
   */
  @Transactional
  public void updateAccount(Long userId, MultipartFile imageFile, AccountRequest accountRequest)
      throws IOException {

    String newEncodedPassword = null;
    String profileImageUrl = null;
    User currentUser = userRepository.findById(userId)
        .orElseThrow(() -> USER_NOT_FOUND.createResponseStatusException());

    //프로필 사진 변경을 요청한 경우
    if (imageFile != null) {
      profileImageUrl = s3FileProcessor.uploadImageFileToS3(bucket, imageFile);
    }

    //비밀번호 변경을 요청한 경우
    if (accountRequest != null) {
      newEncodedPassword = verifyPassword(currentUser.getPassword(), newEncodedPassword,
          accountRequest);
    }

    User user = currentUser.update(profileImageUrl, newEncodedPassword);
    userRepository.save(user);
  }


  /**
   * 유저 정보 반환
   *
   * @param userId
   * @return UserInfoResponse
   */
  public UserInfoResponse getUserInfo(Long userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> USER_NOT_FOUND.createResponseStatusException());
    return UserInfoResponse.builder()
        .userId(user.getId())
        .name(user.getName())
        .role(user.getRole().toString())
        .profileImageUrl(user.getProfileImageUrl())
        .build();
  }

  /**
   * 새 비밀번호 검증
   */
  public String verifyPassword(String currentPassword, String newEncodedPassword,
      AccountRequest accountRequest) {
    if (accountRequest.getNewPassword().isEmpty() || accountRequest.getConfirmPassword()
        .isEmpty()) {
      throw PASSWORD_BLANK.createResponseStatusException();
    }

    if (!accountRequest.getNewPassword().equals(accountRequest.getConfirmPassword())) {
      throw NEW_PASSWORD_NOT_MATCHED.createResponseStatusException();
    }

    if (!passwordEncoder.matches(accountRequest.getCurrentPassword(), currentPassword)) {
      throw PASSWORD_NOT_MATCHED.createResponseStatusException();
    }

    newEncodedPassword = passwordEncoder.encode(accountRequest.getConfirmPassword());
    return newEncodedPassword;
  }

}
