package com.hanahakdangserver.news.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Schema(description = "뉴스 응답")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
@ToString
public class NewsResponse {

  @Schema(description = "뉴스 ID", example = "1")
  private Long id;

  @Schema(description = "뉴스 제목", example = "KB국민은행, ESG 경영 실천 협약 체결")
  private String title;

  @Schema(description = "뉴스 내용", example = "KB국민은행은 ESG 경영을 실천하기 위해...")
  private String content;

  @Schema(description = "뉴스 URL", example = "https://www.fetimes.co.kr/news/articleView.html?idxno=12345")
  private String newsUrl;

  @Schema(description = "뉴스 썸네일 URL", example = "https://cdn.fetimes.co.kr/news/thumbnail/202412/12345.jpg")
  private String newsThumbnailUrl;

  @Schema(description = "작성일시", example = "2024-12-23 10:19")
  private String createdAt;
}
