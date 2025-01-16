package com.hanahakdangserver.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hanahakdangserver.auth.dto.EmailDTO;
import com.hanahakdangserver.auth.dto.LoginDTO;
import com.hanahakdangserver.auth.service.AuthService;
import com.hanahakdangserver.auth.dto.IsSendEmailResponse;
import com.hanahakdangserver.user.dto.MentiSignupRequest;
import com.hanahakdangserver.user.repository.UserRepository;

@RestController
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;
  private final UserRepository userRepository;

  /**
   * 멘티 회원가입 엔드 포인트 멘티 프로필 파일 처리는 추후 구현할 예정
   *
   * @param mentiSignupRequest
   * @return created 응답코드, message
   */
  @PostMapping("/signup/menti")
  public ResponseEntity<String> signupMenti(@RequestBody MentiSignupRequest mentiSignupRequest) {
    return authService.signupMenti(mentiSignupRequest);
  }

  /**
   * 이메일 발송 엔드 포인트
   *
   * @param emailDTO
   * @return 발송여부 Response
   */
  @PostMapping("/send-email")
  public IsSendEmailResponse checkDuplicateEmail(@RequestBody EmailDTO emailDTO) {
    return authService.sendEmail(emailDTO);
  }

  /**
   * 유저가 수신한 이메일 링크에 접속 시 이메일 검증 엔드 포인트
   *
   * @param email
   * @param authToken
   * @return Ok 응답코드, message
   */
  @GetMapping("/verify-email")
  public ResponseEntity<String> verifyEmail(@RequestParam("email") String email,
      @RequestParam("authToken") String authToken) {
    return authService.vertifyEmail(email, authToken);
  }

  /**
   * 유저(멘토,멘티) 로그인
   *
   * @param loginDTO
   * @return Ok 응답코드, message
   */
  @PostMapping("/login")
  public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO) {
    return authService.login(loginDTO);
  }

}
