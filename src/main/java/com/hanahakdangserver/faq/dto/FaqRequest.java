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

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
@ToString
@Schema(description = "FAQ 등록 요청")
public class FaqRequest {

//  @Schema(description = "사용자 ID", example = "1")
//  @NotNull(message = "사용자 ID는 반드시 입력해야 합니다.")
//  private Long userId;

  @Schema(description = "문의 내용", example = "강의 일정에 대해 문의드립니다.")
  @NotBlank(message = "문의 내용은 반드시 입력해야 합니다.")
  private String content;
}
