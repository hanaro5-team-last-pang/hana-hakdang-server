package com.hanahakdangserver.review.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@ToString
@Schema(description = "리뷰 응답 DTO")
public class ReviewResponse {

  @Schema(description = "평균 별점", example = "4.5")
  private String averageScore; // 평균 별점

  @Schema(description = "총 리뷰 수", example = "19")
  private int count; // 전체 리뷰 수

  @Schema(description = "별점별 개수")
  private List<SubScore> subScores; // 별점별 개수 리스트

  @Schema(description = "리뷰 리스트")
  private List<DetailedReview> reviews; // 리뷰 리스트

  @Getter
  @Builder
  @AllArgsConstructor
  public static class SubScore {

    @Schema(description = "별점", example = "5")
    private int score;

    @Schema(description = "별점 개수", example = "10")
    private int count;
  }

  @Getter
  @Builder
  @AllArgsConstructor
  public static class DetailedReview {

    @Schema(description = "리뷰 ID", example = "1")
    private Long id;

    @Schema(description = "강의 ID", example = "101")
    private Long lectureId;

    @Schema(description = "작성자 ID", example = "501")
    private Long userId;

    @Schema(description = "작성자 이름", example = "양지은")
    private String userName;

    @Schema(description = "작성자 프로필 이미지 URL", example = "http://example.com/profile.jpg")
    private String imageUrl;

    @Schema(description = "강의 제목", example = "자바 기본 강의")
    private String lectureTitle;

    @Schema(description = "리뷰 내용", example = "강의가 매우 유익했습니다.")
    private String content;

    @Schema(description = "평점", example = "4.5")
    private int score;

    @Schema(description = "리뷰 작성 시간", example = "2025-01-20T10:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createdAt;

  }
}
