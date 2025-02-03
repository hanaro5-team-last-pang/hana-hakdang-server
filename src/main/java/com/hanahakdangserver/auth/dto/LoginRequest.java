package com.hanahakdangserver.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginRequest {

  @Schema(description = "이메일", example = "user@example.com")
  @NotBlank
  @Email(message = "잘못된 이메일 형식입니다.")
  @NotNull
  private String email;

  @Schema(description = "비밀번호", example = "1234")
  @NotBlank
  @NotNull
  private String password;

}
