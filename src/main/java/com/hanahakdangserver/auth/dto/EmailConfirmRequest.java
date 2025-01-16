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


@Schema(description = "인증 토큰과 이메일을 확인해여 이메일 인증을 시도합니다.")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailConfirmRequest {

  @Schema(description = "이메일", example = "user@example.com")
  @NotBlank
  @Email(message = "잘못된 이메일 형식입니다.")
  @NotNull
  private String email;

  @Schema(description = "인증 토큰")
  @NotBlank
  @NotNull
  private String token;

}
