package com.hanahakdangserver.lecture.enrollment.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Schema(description = "수강 목록 조회 응답")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@ToString
public class MenteeEnrollmentResponse {

  @Schema(description = "전체 수강 목록 개수", example = "30")
  private Long totalCount;

  @Schema(description = "수강 목록")
  private List<MenteeEnrollmentDetailDTO> enrollmentList;
}
