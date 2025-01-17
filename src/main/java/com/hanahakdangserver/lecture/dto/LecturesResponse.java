package com.hanahakdangserver.lecture.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Schema(description = "강의 목록 조회 응답")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@ToString
public class LecturesResponse {

  @Schema(description = "전체 강의 개수", example = "20")
  private Long total_count;

  @Schema(description = "강의 목록")
  private List<LectureDetailDTO> lecture_list;
}
