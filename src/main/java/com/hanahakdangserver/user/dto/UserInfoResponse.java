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

  @Schema(description = "유저역할", example = "MENTEE")
  private String role;

  @Schema(description = "유저 프로필 사진", example = "https://lastpang-file-bucket.s3.ap-northeast-2.amazonaws.com/55de2de7-58e1-4a1d-a682-d4572b6da31d포차코.png")
  private String profileImageUrl;
}
