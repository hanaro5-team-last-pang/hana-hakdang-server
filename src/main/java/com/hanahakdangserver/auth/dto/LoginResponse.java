package com.hanahakdangserver.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Schema(description = "로그인 성공 응답")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@ToString
public class LoginResponse {

  @Schema(description = "Access Token", example = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ5amU5ODAyQG5hdmVyLmNvbSIsImlhdCI6MTczODYyODE4MiwiZXhwIjoxNzM4NjMxNzgyfQ.oJHrNRnGHTFVzqN9AJJA44H73IB58LGIz0S6DcqZJpMQ89SQrnSiqsRyk97VgdZfehCqmtHiDEANVLK0uduJIw")
  private String accessToken;

}
