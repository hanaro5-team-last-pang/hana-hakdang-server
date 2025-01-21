package com.hanahakdangserver.faq.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
@Schema(description = "답변 요청 DTO")
public class AnswerRequest {
  

  @Schema(description = "문의 ID", example = "1", required = true)
  @NotNull(message = "문의 ID는 반드시 입력해야 합니다.")
  private Long faqId;

  @Schema(description = "답변 내용", example = "질문 내용에 대한 답변입니다.", required = true)
  @NotBlank(message = "답변 내용은 반드시 입력해야 합니다.")
  private String content;
}
