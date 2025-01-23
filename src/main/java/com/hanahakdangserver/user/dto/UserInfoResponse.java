package com.hanahakdangserver.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Schema(description = "유저 정보 반환")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
@ToString
public class UserInfoResponse {

  @Schema(description = "유저ID", example = "111")
  private Long userId;

  @Schema(description = "유저이름", example = "한성민")
  private String name;
}
