package com.hanahakdangserver.auth.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hanahakdangserver.auth.dto.EmailCheckRequest;
import com.hanahakdangserver.auth.dto.EmailConfirmRequest;
import com.hanahakdangserver.auth.dto.LoginRequest;
import com.hanahakdangserver.auth.dto.MenteeSignupRequest;
import com.hanahakdangserver.auth.dto.MentorSignupRequest;
import com.hanahakdangserver.auth.service.AuthService;
import com.hanahakdangserver.card.service.CardService;
import com.hanahakdangserver.common.ResponseDTO;
import static com.hanahakdangserver.auth.enums.AuthResponseSuccessEnum.EMAIL_CHECK_SEND_SUCCESS;
import static com.hanahakdangserver.auth.enums.AuthResponseSuccessEnum.EMAIL_CONFIRMED;
import static com.hanahakdangserver.auth.enums.AuthResponseSuccessEnum.LOG_IN_SUCCESS;
import static com.hanahakdangserver.auth.enums.AuthResponseSuccessEnum.SIGN_UP_SUCCESS;


@Tag(name = "인증 API", description = "인증 관련 API 목록")
@RestController
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;
  private final CardService cardService;

  @Operation(summary = "멘티 회원 가입", description = "멘티로 회원 가입 요청합니다.")
  @PostMapping("/signup/mentee")
  public ResponseEntity<ResponseDTO<Object>> signupMentee(
      @Valid @RequestBody MenteeSignupRequest menteeSignupRequest) {
    authService.signupMentee(menteeSignupRequest);
    return SIGN_UP_SUCCESS.createResponseEntity();
  }

  @Operation(summary = "멘토 회원 가입", description = "멘토(행원)로 회원 가입 요청합니다.")
  @PostMapping("/signup/mentor")
  public ResponseEntity<ResponseDTO<Object>> signupMentor(
      @Valid @RequestBody MentorSignupRequest mentorSignupRequest) {
    authService.signUpMentor(mentorSignupRequest);
    cardService.create(mentorSignupRequest.getEmail());
    return SIGN_UP_SUCCESS.createResponseEntity();
  }

  @Operation(summary = "이메일 인증 요청", description = "이메일 인증을 요청합니다. 인증을 위해 이메일로 메일이 전송됩니다.")
  @PostMapping("/send-email")
  public ResponseEntity<ResponseDTO<Object>> checkDuplicateEmail(
      @Valid @RequestBody EmailCheckRequest emailCheckRequest) {
    authService.sendEmail(emailCheckRequest);
    return EMAIL_CHECK_SEND_SUCCESS.createResponseEntity();
  }

  @Operation(summary = "이메일 인증 확인 요청", description = "이메일로 전송된 링크를 통해 요청되는 확인 절차입니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "이메일 인증 성공"),
      @ApiResponse(responseCode = "400", description = "이메일 인증이 완료되지 않았습니다.")
  })
  @PostMapping("/verify-email")
  public ResponseEntity<ResponseDTO<Object>> verifyEmail(@RequestParam String email,
      @RequestParam String authToken) {
    authService.verifyEmail(email, authToken);
    return EMAIL_CONFIRMED.createResponseEntity();
  }

}
