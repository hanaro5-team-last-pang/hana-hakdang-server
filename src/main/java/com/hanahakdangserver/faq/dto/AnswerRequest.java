package com.hanahakdangserver.faq.dto;

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
public class AnswerRequest {

  @NotNull(message = "유저 ID는 필수입니다.")
  private Long userId;

  private Long faqId;

  @NotBlank(message = "답변 내용은 필수입니다.")
  private String content;
}
