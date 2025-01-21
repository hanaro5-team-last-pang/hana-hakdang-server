package com.hanahakdangserver.review.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
@Schema(description = "리뷰 응답 DTO")
public class ReviewResponse {

  @Schema(description = "리뷰 ID", example = "1")
  private Long id; // 리뷰 ID

  @Schema(description = "강의 ID", example = "101")
  private Long lectureId; // 강의 ID

  @Schema(description = "작성자 ID", example = "501")
  private Long userId; // 작성자 ID

  @Schema(description = "작성자 이름", example = "양지은")
  private String userName; // 작성자 이름

  @Schema(description = "강의 제목", example = "자바 기본 강의")
  private String lectureTitle; // 강의 제목

  @Schema(description = "리뷰 내용", example = "강의가 매우 유익했습니다.")
  private String content; // 리뷰 내용

  @Schema(description = "평점", example = "4.5")
  private Double score; // 평점

  @Schema(description = "리뷰 작성 시간", example = "2025년 01월 20일")
  @JsonFormat(pattern = "yyyy년 MM월 dd일", timezone = "Asia/Seoul")
  private String createdAt; // 작성 시간 (yyyy년 MM월 dd일)

  @Schema(description = "리뷰 수정 시간", example = "2025냔 01월 21일")
  @JsonFormat(pattern = "yyyy년 MM월 dd일", timezone = "Asia/Seoul")
  private String updatedAt; // 수정 시간 (yyyy년 MM월 dd일)
}
