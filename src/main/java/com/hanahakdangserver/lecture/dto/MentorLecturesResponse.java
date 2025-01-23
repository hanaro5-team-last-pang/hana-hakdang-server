package com.hanahakdangserver.lecture.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Schema(description = "등록한 강의 목록 조회 응답")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@ToString
public class MentorLecturesResponse {

  @Schema(description = "전체 강의 개수", example = "20")
  private Long totalCount;

  @Schema(description = "강의 목록")
  private MentorLecturesFilterDTO lectureList;

}
