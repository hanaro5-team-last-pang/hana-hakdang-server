package com.hanahakdangserver.lecture.enrollment.dto;

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

@Schema(description = "수강 목록 상세 응답")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@ToString
public class MenteeEnrollmentDetailDTO {

  @Schema(description = "강의실 Id", example = "16259194734854144")
  private String classroomId;

  @Schema(description = "강의 Id", example = "1")
  private Long lectureId;

  @Schema(description = "수강신청 Id", example = "1")
  private Long enrollmentId;

  @Schema(description = "멘토 Id", example = "1")
  private Long mentorId;

  @Schema(description = "멘토 이름", example = "정중일")
  private String mentorName;

  @Schema(description = "강의 명칭", example = "정중일과 함께 하는 하나원큐앱 정복하기")
  private String title;

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

  @Schema(description = "수강신청 취소 여부", example = "false")
  private Boolean isEnrollmentCanceled;

  @Schema(description = "강의 완료 여부", example = "false")
  private Boolean isDone;

  @Schema(description = "강의 취소 여부", example = "false")
  private Boolean isLectureCanceled;

  @Schema(description = "강의실 입장 가능 여부", example = "false")
  private Boolean possibleToEnter;

}
