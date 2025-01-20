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

  private Long id;
  private String title;
  private String content;
  private String newsUrl;
  private String newsThumbnailUrl;
  private String createdAt;
}
