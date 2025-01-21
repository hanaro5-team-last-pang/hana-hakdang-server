package com.hanahakdangserver.classroom.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Schema(description = "강의살 입장 요청 응답")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
@ToString
public class ClassroomEnterResponse {

  @Schema(description = "강의실 입장 비밀번호")
  private String password;

  @Schema(description = "멘토 ID")
  private Long mentorId;

  @Schema(description = "강의 ID")
  private Long lectureId;

}
