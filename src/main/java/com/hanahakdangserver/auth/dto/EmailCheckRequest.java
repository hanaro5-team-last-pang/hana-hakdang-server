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


@Schema(description = "이메일 중복 확인 및 인증을 위한 링크가 담긴 메일 전송합니다.")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailCheckRequest {

  @Schema(description = "이메일", example = "user@example.com")
  @NotBlank
  @NotNull
  @Email(message = "잘못된 이메일 형식입니다.")
  private String email;

}
