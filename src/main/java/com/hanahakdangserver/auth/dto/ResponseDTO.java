package com.hanahakdangserver.auth.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Schema(description = "기본 응답 형식입니다.")
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResponseDTO<T> {

  @Schema(description = "응답 메시지")
  private String message;

  @Schema(description = "응답 결과", nullable = true)
  private T result;
}
