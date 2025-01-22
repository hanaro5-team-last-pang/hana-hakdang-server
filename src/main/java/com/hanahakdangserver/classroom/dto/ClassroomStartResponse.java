package com.hanahakdangserver.classroom.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Schema(description = "강의 시작 요청 응답")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
@ToString
public class ClassroomStartResponse {

  @Schema(description = "강의실 입장 비밀번호. 웹소켓 연결 시 사용됩니다.")
  private String password;

  @Schema(description = "강의 ID")
  private Long lectureId;

}
