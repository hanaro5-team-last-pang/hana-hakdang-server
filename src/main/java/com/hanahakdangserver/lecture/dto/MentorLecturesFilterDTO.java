package com.hanahakdangserver.lecture.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Schema(description = "멘토 등록 강의 분류")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@ToString
public class MentorLecturesFilterDTO {

  @Schema(description = "시작 가능한 강의 목록")
  private List<MentorLectureDetailDTO> ableToStart;

  @Schema(description = "진행 예정인 강의 목록")
  private List<MentorLectureDetailDTO> notStarted;

  @Schema(description = "완료된 강의 목록")
  private List<MentorLectureDetailDTO> done;
}
