package com.hanahakdangserver.lecture.dto;

import java.time.LocalDateTime;

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

@Schema(description = "멘토 등록 강의 상세 정보")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@ToString
public class MentorLectureDetailDTO {

  @Schema(description = "강의실 Id", example = "16259194734854144")
  private Long classroomId;

  @Schema(description = "강의 Id", example = "1")
  private Long lectureId;

  @Schema(description = "강의 명칭", example = "정중일과 함께 하는 하나원큐앱 정복하기")
  private String title;

  @Schema(description = "강의 취소 여부", example = "false")
  private Boolean isCanceled;

  @Schema(description = "강의 완료 여부", example = "false")
  private Boolean isDone;

  @Schema(description = "강의 시작 가능 여부", example = "false")
  private Boolean ableToStart;

  @Schema(description = "강의 시작시간", example = "2025-01-19 11:30:00")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  private LocalDateTime startTime;

  @Schema(description = "강의 종료시간", example = "2025-01-19 12:30:00")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  private LocalDateTime endTime;

}
