package com.hanahakdangserver.review.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
@Schema(description = "리뷰 요청 DTO")
public class ReviewRequest {

  @Schema(description = "리뷰 작성자 ID", example = "1")
  @NotNull(message = "리뷰 작성자 ID는 반드시 입력해야 합니다.")
  private Long userId; // 리뷰 작성자 ID

  @Schema(description = "리뷰 내용", example = "강의 내용이 너무 좋았습니다.")
  @NotBlank(message = "리뷰 내용은 비워둘 수 없습니다.")
  private String content; // 리뷰 내용

  @Schema(description = "평점 (1~10)", example = "5")
  @NotNull(message = "평점은 반드시 입력해야 합니다.")
  @Min(value = 1, message = "평점은 최소 1이어야 합니다.")
  @Max(value = 5, message = "평점은 최대 10이어야 합니다.")
  private Integer score; // 평점
}
