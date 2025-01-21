package com.hanahakdangserver.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Schema(description = "계정 정보 수정 요청")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
@ToString
public class AccountRequest {

  @Schema(description = "현재 비밀번호", example = "1234")
  private String currentPassword;

  @Schema(description = "새 비밀번호", example = "5678")
  private String newPassword;

  @Schema(description = "새 비밀번호 확인", example = "5678")
  private String confirmPassword;

}
