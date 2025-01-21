package com.hanahakdangserver.faq.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@ToString
@Schema(description = "FAQ 응답 DTO")
public class FaqResponse {

  @Schema(description = "FAQ ID", example = "1")
  private Long id;

  @Schema(description = "유저 이름", example = "육지은")
  private String userName;

  @Schema(description = "문의 내용", example = "이 강의의 수업 자료는 어디서 받을 수 있나요?")
  private String content;

  @Schema(description = "문의 생성 시간", example = "2025년 01월 01")
  private String createdAt;

  @Schema(description = "답변 리스트")
  private List<AnswerResponse> answers;
}
