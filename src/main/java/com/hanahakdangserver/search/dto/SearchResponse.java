package com.hanahakdangserver.search.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Schema(description = "검색 결과 조회 응답")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@ToString
public class SearchResponse {

  @Schema(description = "전체 검색 결과 개수", example = "20")
  private Long totalCount;

  @Schema(description = "검색 결과 목록")
  private List<LectureResultDetailDTO> lectureList;
}
