package com.hanahakdangserver.auth.dto;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "멘티 회원 가입 요청")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MenteeSignupRequest {

  @Schema(description = "이메일")
  @NotBlank
  @Email(message = "잘못된 이메일 형식입니다.")
  @NotNull
  private String email;

  @Schema(description = "이름")
  @NotNull
  @NotBlank
  private String name;

  @Schema(description = "비밀번호")
  @NotNull
  @NotBlank
  private String password;

  @Schema(description = "비밀번호 확인")
  @NotNull
  @NotBlank
  private String confirmedPassword;

  @Schema(description = "생년월일")
  @NotNull
  @NotBlank
  private LocalDate birth;

}
