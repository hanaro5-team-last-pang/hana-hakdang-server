package com.hanahakdangserver.faq.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@ToString
@Schema(description = "답변 응답 DTO")
public class AnswerResponse {

  @Schema(description = "답변 ID", example = "1")
  private Long id;

  @Schema(description = "유저 이름", example = "한성민")
  private String userName;

  @Schema(description = "답변 내용", example = "답변 내용입니다")
  private String content;

  @Schema(description = "답변 생성 시간", example = "yyyy년 MM월 dd일")
  private String createdAt;
}
